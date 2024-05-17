package com.desofs.backend.controllers;

import com.desofs.backend.dtos.CreateBookingDto;
import com.desofs.backend.dtos.CreateRentalPropertyDto;
import com.desofs.backend.dtos.FetchBookingDto;
import com.desofs.backend.dtos.FetchRentalPropertyDto;
import com.desofs.backend.exceptions.DatabaseException;
import com.desofs.backend.exceptions.NotAuthorizedException;
import com.desofs.backend.exceptions.NotFoundException;
import com.desofs.backend.services.BookingService;
import com.desofs.backend.services.RentalPropertyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Booking Controller", description = "Endpoints for managing bookings")
@RestController
@RequestMapping(path = "/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    public ResponseEntity<FetchBookingDto> getById(@RequestParam final String propertyId,
                                                   @RequestParam final String bookingId) throws NotFoundException {
        FetchBookingDto rentalProperty = this.bookingService.findById(propertyId, bookingId);

        return ResponseEntity.ok().body(rentalProperty);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<FetchBookingDto> create(@RequestBody CreateBookingDto createBookingDto)
            throws DatabaseException, NotFoundException {

        FetchBookingDto bookingDto = this.bookingService.create(createBookingDto);
        return new ResponseEntity<>(bookingDto, HttpStatus.CREATED);
    }
}
