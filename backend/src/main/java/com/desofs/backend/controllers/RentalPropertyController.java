package com.desofs.backend.controllers;

import com.desofs.backend.exceptions.DatabaseException;
import com.desofs.backend.dtos.CreateRentalPropertyDto;
import com.desofs.backend.dtos.FetchRentalPropertyDto;
import com.desofs.backend.services.RentalPropertyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Rental Property", description = "Endpoints for managing rental properties")
@RestController
@RequestMapping(path = "/rental_property")
@RequiredArgsConstructor
public class RentalPropertyController {

    private final RentalPropertyService rentalPropertyService;

    @GetMapping("/{id}")
    public ResponseEntity<FetchRentalPropertyDto> getById(@PathVariable final String id) {
        FetchRentalPropertyDto rentalProperty = this.rentalPropertyService.findById(id);
        return ResponseEntity.ok().body(rentalProperty);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<FetchRentalPropertyDto> create(@RequestBody CreateRentalPropertyDto createRentalPropertyDto,
                                                         Authentication authentication) throws DatabaseException {
        String userId = authentication.getName();
        FetchRentalPropertyDto rentalProperty = this.rentalPropertyService.create(createRentalPropertyDto, userId);
        return new ResponseEntity<>(rentalProperty, HttpStatus.CREATED);
    }
}
