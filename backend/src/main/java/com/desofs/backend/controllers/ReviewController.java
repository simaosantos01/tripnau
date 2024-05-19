package com.desofs.backend.controllers;

import com.desofs.backend.dtos.CreateReviewDto;
import com.desofs.backend.dtos.FetchReviewDto;
import com.desofs.backend.exceptions.DatabaseException;
import com.desofs.backend.exceptions.NotAuthorizedException;
import com.desofs.backend.exceptions.NotFoundException;
import com.desofs.backend.services.ReviewService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Review Controller", description = "Endpoints for managing reviews")
@RestController
@RequestMapping(path = "/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<FetchReviewDto> create(@RequestBody CreateReviewDto createReviewDto,
                                                 Authentication authentication)
            throws DatabaseException, NotFoundException, NotAuthorizedException {

        String userId = authentication.getName();
        FetchReviewDto reviewDto = this.reviewService.create(createReviewDto, userId);
        return new ResponseEntity<>(reviewDto, HttpStatus.CREATED);
    }

    @PostMapping("/change_state")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@RequestParam String reviewId, @RequestParam String state)
            throws DatabaseException, NotFoundException {

        FetchReviewDto reviewDto = this.reviewService.changeState(reviewId, state);
        return new ResponseEntity<>(reviewDto, HttpStatus.OK);
    }
}