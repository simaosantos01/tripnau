package com.desofs.backend.services;

import com.desofs.backend.database.mappers.ReviewMapper;
import com.desofs.backend.database.repositories.ReviewRepository;
import com.desofs.backend.domain.aggregates.ReviewDomain;
import com.desofs.backend.domain.enums.ReviewState;
import com.desofs.backend.dtos.CreateReviewDto;
import com.desofs.backend.dtos.FetchReviewDto;
import com.desofs.backend.exceptions.DatabaseException;
import com.desofs.backend.exceptions.NotFoundException;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component("ReviewService")
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final ReviewMapper reviewMapper;

    public ReviewService(ReviewRepository reviewRepository, ReviewMapper reviewMapper) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
    }

    @Transactional
    public FetchReviewDto create(CreateReviewDto createReviewDto, String authorId) throws DatabaseException {
        ReviewDomain reviewDomain = new ReviewDomain(createReviewDto, authorId);

        this.reviewRepository.create(reviewDomain);

        return reviewMapper.domainToDto(reviewDomain);
    }

    @Transactional
    public FetchReviewDto findById(String reviewId) throws NotFoundException {
        ReviewDomain reviewDomain = this.reviewRepository.findById(reviewId);
        if (reviewDomain == null) {
            throw new NotFoundException("Review not found");
        }

        return reviewMapper.domainToDto(reviewDomain);
    }

    @Transactional
    public FetchReviewDto changeState(String reviewId, String state) throws NotFoundException, DatabaseException {
        ReviewDomain reviewDomain = this.reviewRepository.findById(reviewId);

        if (reviewDomain == null) {
            throw new NotFoundException("Review not found");
        }

        if (!EnumUtils.isValidEnum(ReviewState.class, state)) {
            throw new NotFoundException("State is not valid");
        }

        reviewDomain.changeState(ReviewState.valueOf(state));
        this.reviewRepository.update(reviewDomain);

        return this.reviewMapper.domainToDto(reviewDomain);
    }
}
