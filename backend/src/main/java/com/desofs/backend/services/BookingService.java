package com.desofs.backend.services;

import com.desofs.backend.database.mappers.BookingMapper;
import com.desofs.backend.database.mappers.PaymentMapper;
import com.desofs.backend.database.repositories.BookingRepository;
import com.desofs.backend.database.repositories.PaymentRepository;
import com.desofs.backend.database.repositories.RentalPropertyRepository;
import com.desofs.backend.domain.aggregates.BookingDomain;
import com.desofs.backend.domain.aggregates.RentalPropertyDomain;
import com.desofs.backend.domain.aggregates.ReviewDomain;
import com.desofs.backend.domain.valueobjects.Id;
import com.desofs.backend.dtos.CreateBookingDto;
import com.desofs.backend.dtos.FetchBookingDto;
import com.desofs.backend.dtos.FetchReviewDto;
import com.desofs.backend.exceptions.DatabaseException;
import com.desofs.backend.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {

    private final RentalPropertyRepository rentalPropertyRepository;

    private final PaymentRepository paymentRepository;

    private final BookingRepository bookingRepository;

    private final BookingMapper bookingMapper;

    private final PaymentMapper paymentMapper;

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

    @Transactional
    public FetchBookingDto create(CreateBookingDto bookingDto) throws DatabaseException, NotFoundException {
        RentalPropertyDomain rentalProperty = getPropertyThrowingError(bookingDto.getPropertyId());

        Id bookingId = Id.create(UUID.randomUUID().toString());
        BookingDomain bookingDomain = new BookingDomain(bookingDto, bookingId);
        rentalProperty.addBooking(bookingDomain);

        bookingRepository.create(bookingDomain, rentalProperty.getId());
        paymentRepository.create(paymentMapper.dtoToDomain(bookingDto.getPayment(), bookingId.value()));

        return this.bookingMapper.domainToDto(bookingDomain);
    }

    @Transactional
    public FetchBookingDto cancel(String bookingId) throws NotFoundException {
        BookingDomain bookingDomain = this.getBookingThrowingError(bookingId);

        bookingDomain = bookingDomain.cancel();

        bookingRepository.updateEvents(bookingDomain);

        return this.bookingMapper.domainToDto(bookingDomain);
    }

    @Transactional
    public List<FetchBookingDto> findAllByUserId(String userId) throws NotFoundException {
        List<BookingDomain> bookingDomain = this.bookingRepository.findAllByUserId(userId);

        return bookingDomain.stream().map(this.bookingMapper::domainToDto).toList();
    }

    @Transactional
    public FetchBookingDto findById(String bookingId) throws NotFoundException {
        BookingDomain bookingDomain = getBookingThrowingError(bookingId);

        return bookingMapper.domainToDto(bookingDomain);
    }

    @Scheduled(cron = "* * 3 * * *")
    public void checkBookingsThatCheckoutPassed() {
        try {
            var count = this.bookingRepository.clearBookingWhereCheckoutDatePassed();
        } catch (Exception ignored) {
        }
    }

}
