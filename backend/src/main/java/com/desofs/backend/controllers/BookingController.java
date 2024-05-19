package com.desofs.backend.controllers;

import com.desofs.backend.dtos.*;
import com.desofs.backend.exceptions.DatabaseException;
import com.desofs.backend.exceptions.NotFoundException;
import com.desofs.backend.services.BookingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Booking Controller", description = "Endpoints for managing bookings")
@RestController
@RequestMapping(path = "/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

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

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<FetchBookingDto> create(@RequestBody final CreateBookingDto createBookingDto, Authentication authentication)
            throws DatabaseException, NotFoundException {

        String userId = authentication.getName();
        FetchBookingDto bookingDto = this.bookingService.create(createBookingDto, userId);
        return new ResponseEntity<>(bookingDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<FetchBookingDto> cancel(@PathVariable final String id)
            throws NotFoundException {

        FetchBookingDto bookingDto = this.bookingService.cancel(id);

        return new ResponseEntity<>(bookingDto, HttpStatus.CREATED);
    }

}
