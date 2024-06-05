package com.desofs.backend.services;

import com.desofs.backend.database.mappers.BookingMapper;
import com.desofs.backend.database.mappers.PaymentMapper;
import com.desofs.backend.database.repositories.BookingRepository;
import com.desofs.backend.database.repositories.PaymentRepository;
import com.desofs.backend.database.repositories.RentalPropertyRepository;
import com.desofs.backend.domain.aggregates.BookingDomain;
import com.desofs.backend.domain.aggregates.RentalPropertyDomain;
import com.desofs.backend.domain.valueobjects.Id;
import com.desofs.backend.domain.valueobjects.IntervalTime;
import com.desofs.backend.domain.valueobjects.MoneyAmount;
import com.desofs.backend.dtos.*;
import com.desofs.backend.exceptions.DatabaseException;
import com.desofs.backend.exceptions.NotFoundException;
import com.desofs.backend.exceptions.UnavailableTimeInterval;
import com.desofs.backend.utils.IntervalTimeUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData.ProductData;
import com.stripe.param.checkout.SessionCreateParams.PaymentIntentData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {

    private final RentalPropertyRepository rentalPropertyRepository;

    private final PaymentRepository paymentRepository;

    private final BookingRepository bookingRepository;

    private final BookingMapper bookingMapper;

    private final PaymentMapper paymentMapper;

    @Value("${stripe.apiKey}")
    private String StripeApiKey;

    /**
     * Gets the property or throws exception if not found
     */
    private RentalPropertyDomain getPropertyThrowingError(String propertyId) throws NotFoundException {
        RentalPropertyDomain rentalProperty = this.rentalPropertyRepository.findById(propertyId);
        if (rentalProperty == null) {
            throw new NotFoundException("Rental property not found");
        }
        return rentalProperty;
    }

    /*
     * Gets the booking or throws exception if not found
     */
    private BookingDomain getBookingThrowingError(String bookingId) throws NotFoundException {
        BookingDomain bookingDomain = this.bookingRepository.findById(bookingId);
        if (bookingDomain == null) {
            throw new NotFoundException("Booking not found");
        }
        return bookingDomain;
    }

    private String requestCheckoutSession(long totalPrice, RentalPropertyDomain rentalProperty, String successUrl,
                                          IntervalTimeDto intervalTime, String userId) throws StripeException, JsonProcessingException {

        ProductData productData = PriceData.ProductData.builder()
                .setName(rentalProperty.getPropertyName().value())
                .build();

        PriceData priceData = PriceData.builder()
                .setCurrency("eur")
                .setUnitAmount(totalPrice * 100)
                .setProductData(productData)
                .build();

        CheckoutSessionMetadata metadata = new CheckoutSessionMetadata(rentalProperty.getId().value(), intervalTime, userId);
        ObjectMapper objectMapper = new ObjectMapper();
        PaymentIntentData paymentData = PaymentIntentData.builder()
                .putMetadata("data", objectMapper.writeValueAsString(metadata)).build();

        Stripe.apiKey = this.StripeApiKey;
        SessionCreateParams params = SessionCreateParams.builder()
                .setSuccessUrl(successUrl)
                .addLineItem(SessionCreateParams.LineItem.builder().setPriceData(priceData).setQuantity(1L).build())
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setPaymentIntentData(paymentData)
                .build();

        Session session = Session.create(params);
        return session.getUrl();
    }

    public FetchStripeSessionDto createStripeCheckoutSession(CreateStripeSessionDto createCheckoutDto, String userId)
            throws NotFoundException, UnavailableTimeInterval, StripeException, JsonProcessingException {

        RentalPropertyDomain rentalProperty = getPropertyThrowingError(createCheckoutDto.propertyId());
        List<IntervalTime> unavailableIntervals = rentalProperty.getBookingList().stream()
                .map(BookingDomain::getIntervalTime).toList();

        // Check if requested date interval intercepts with any date already scheduled
        Date from = createCheckoutDto.intervalTime().getFrom();
        Date to = createCheckoutDto.intervalTime().getTo();
        IntervalTime intervalToCompare = IntervalTime.create(from, to);
        for (var interval : unavailableIntervals) {
            if (IntervalTimeUtils.intervalsIntercept(interval, intervalToCompare)) {
                throw new UnavailableTimeInterval();
            }
        }

        // calculate price for the stay
        MoneyAmount defaultNightPrice = rentalProperty.getDefaultNightPrice();
        long days = Duration.between(intervalToCompare.getFrom().toInstant(), intervalToCompare.getTo().toInstant()).toDays();
        long totalPrice = days * defaultNightPrice.getValue().longValue();

        // create strip checkout session
        String sessionUrl = requestCheckoutSession(totalPrice, rentalProperty, createCheckoutDto.successUrl(),
                createCheckoutDto.intervalTime(), userId);
        return new FetchStripeSessionDto(sessionUrl);
    }

    @Transactional
    public void create(PaymentIntent intent) throws DatabaseException, NotFoundException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        CheckoutSessionMetadata metadata = objectMapper.readValue(intent.getMetadata().get("data"),
                CheckoutSessionMetadata.class);

        RentalPropertyDomain rentalProperty = getPropertyThrowingError(metadata.propertyId());
        Id bookingId = Id.create(UUID.randomUUID().toString());
        BookingDomain bookingDomain = new BookingDomain(metadata.intervalTime(), bookingId, metadata.userId());
        rentalProperty.addBooking(bookingDomain);
        bookingRepository.create(bookingDomain, rentalProperty.getId());
    }

    @Transactional
    public FetchBookingDto cancel(String bookingId) throws NotFoundException {
        BookingDomain bookingDomain = this.getBookingThrowingError(bookingId);
        RentalPropertyDomain rentalPropertyDomain = this.bookingRepository.findRentalProperty(bookingId);

        bookingDomain = bookingDomain.cancel();

        bookingRepository.updateEvents(bookingDomain);

        return this.bookingMapper.domainToDto(bookingDomain, rentalPropertyDomain.getId().value());
    }

    @Transactional
    public List<FetchBookingDto> findAllByUserId(String userId) throws NotFoundException {
        List<BookingDomain> bookingDomain = this.bookingRepository.findAllByUserId(userId);

        return bookingDomain.stream().map(booking -> {
            RentalPropertyDomain rentalPropertyDomain;
            try {
                rentalPropertyDomain = this.bookingRepository.findRentalProperty(booking.getId().value());
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
            return this.bookingMapper.domainToDto(booking, rentalPropertyDomain.getId().value());
        }).toList();
    }

    @Transactional
    public FetchBookingDto findById(String bookingId) throws NotFoundException {
        BookingDomain bookingDomain = getBookingThrowingError(bookingId);
        RentalPropertyDomain rentalPropertyDomain = this.bookingRepository.findRentalProperty(bookingId);

        return bookingMapper.domainToDto(bookingDomain, rentalPropertyDomain.getId().value());
    }

    @Scheduled(cron = "* * 3 * * *")
    public void checkBookingsThatCheckoutPassed() {
        try {
            var count = this.bookingRepository.clearBookingWhereCheckoutDatePassed();
        } catch (Exception ignored) {
        }
    }

}
