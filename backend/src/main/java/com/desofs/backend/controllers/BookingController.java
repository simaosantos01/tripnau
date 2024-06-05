package com.desofs.backend.controllers;

import com.desofs.backend.dtos.*;
import com.desofs.backend.exceptions.DatabaseException;
import com.desofs.backend.exceptions.NotFoundException;
import com.desofs.backend.exceptions.UnavailableTimeInterval;
import com.desofs.backend.services.BookingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.net.ApiResource;
import com.stripe.net.Webhook;
import com.stripe.param.forwarding.RequestCreateParams;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Booking Controller", description = "Endpoints for managing bookings")
@RestController
@RequestMapping(path = "/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @Value("${stripe.webhookSecret}")
    private String endpointSecret;

    @GetMapping("/{id}")
    public ResponseEntity<FetchBookingDto> getById(@PathVariable final String id) throws NotFoundException {
        FetchBookingDto booking = this.bookingService.findById(id);

        return ResponseEntity.ok().body(booking);
    }

    @GetMapping("/getAllByUser/{id}")
    public ResponseEntity<List<FetchBookingDto>> getAllByUserId(@PathVariable final String id) throws NotFoundException {
        List<FetchBookingDto> rentalProperty = this.bookingService.findAllByUserId(id);

        return ResponseEntity.ok().body(rentalProperty);
    }

    @PostMapping("/stripe-checkout")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<FetchStripeSessionDto> createCheckout(@RequestBody final CreateStripeSessionDto createCheckoutDto,
                                                                Authentication authentication)
            throws NotFoundException, StripeException, UnavailableTimeInterval, JsonProcessingException {

        String userId = authentication.getName();
        FetchStripeSessionDto sessionDto = this.bookingService.createStripeCheckoutSession(createCheckoutDto, userId);
        return new ResponseEntity<>(sessionDto, HttpStatus.OK);
    }

    @PostMapping("/stripe-webhook")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<FetchBookingDto> create(final HttpServletRequest request)
            throws DatabaseException, NotFoundException, IOException, SignatureVerificationException {

        String payload = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        String sigHeader = request.getHeader("Stripe-Signature");
        Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();

        if (event.getType().equals("payment_intent.succeeded")) {
            PaymentIntent paymentIntent = (PaymentIntent) dataObjectDeserializer.getObject().get();
            this.bookingService.create(paymentIntent);
        } else {
            System.out.println("Unhandled event type: " + event.getType());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<FetchBookingDto> cancel(@PathVariable final String id)
            throws NotFoundException {

        FetchBookingDto bookingDto = this.bookingService.cancel(id);

        return new ResponseEntity<>(bookingDto, HttpStatus.CREATED);
    }

}
