package com.desofs.backend.services;

import com.desofs.backend.database.mappers.ReviewMapper;
import com.desofs.backend.database.repositories.ReviewRepository;
import com.desofs.backend.domain.aggregates.ReviewDomain;
import com.desofs.backend.domain.enums.ReviewState;
import com.desofs.backend.dtos.CreateReviewDto;
import com.desofs.backend.dtos.FetchReviewDto;
import com.desofs.backend.exceptions.DatabaseException;
import com.desofs.backend.exceptions.NotAuthorizedException;
import com.desofs.backend.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final LoggerService logger;

    @Transactional
    public FetchReviewDto create(CreateReviewDto createReviewDto, String userId) throws DatabaseException, NotAuthorizedException {
        try {
            ReviewDomain reviewDomain = new ReviewDomain(createReviewDto, userId);
            this.reviewRepository.create(reviewDomain, createReviewDto.getBookingId(), userId);

            logger.info("Review created by user " + userId + " for booking " + createReviewDto.getBookingId());
            return reviewMapper.domainToDto(reviewDomain);
        } catch (Exception e) {
            logger.error("Failed to create review by user " + userId);
            throw e;
        }
    }

    @Transactional
    public FetchReviewDto findById(String reviewId) throws NotFoundException {
        try {
            ReviewDomain reviewDomain = this.reviewRepository.findById(reviewId);
            if (reviewDomain == null) {
                throw new NotFoundException("Review not found");
            }

            logger.info("Review ID " + reviewId + " found");
            return reviewMapper.domainToDto(reviewDomain);
        } catch (NotFoundException e) {
            logger.warn("Review of ID " + reviewId + " not found");
            throw e;
        } catch (Exception e) {
            logger.error("Error finding review " + reviewId);
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public FetchReviewDto changeState(String reviewId, String state) throws NotFoundException, DatabaseException {
        try {
            ReviewDomain reviewDomain = this.reviewRepository.findById(reviewId);
            if (reviewDomain == null) {
                throw new NotFoundException("Review not found");
            }

            if (!EnumUtils.isValidEnum(ReviewState.class, state)) {
                throw new NotFoundException("State is not valid");
            }

            reviewDomain.changeState(ReviewState.valueOf(state));
            this.reviewRepository.update(reviewDomain);

            logger.info("Review " + reviewId + " state changed to " + state);
            return this.reviewMapper.domainToDto(reviewDomain);
        } catch (NotFoundException e) {
            logger.warn("Failed to change state review " + reviewId);
            throw e;
        } catch (DatabaseException e) {
            logger.error("Database error changing state for review " + reviewId);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error changing state for review " + reviewId);
            throw new RuntimeException(e);
        }
    }
}
