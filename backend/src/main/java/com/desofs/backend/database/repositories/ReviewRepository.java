package com.desofs.backend.database.repositories;

import com.desofs.backend.database.mappers.PaymentMapper;
import com.desofs.backend.database.mappers.ReviewMapper;
import com.desofs.backend.database.models.ImageUrlDB;
import com.desofs.backend.database.models.PaymentDB;
import com.desofs.backend.database.models.ReviewDB;
import com.desofs.backend.database.springRepositories.ImageRepositoryJpa;
import com.desofs.backend.database.springRepositories.PaymentRepositoryJPA;
import com.desofs.backend.database.springRepositories.ReviewRepositoryJPA;
import com.desofs.backend.domain.aggregates.ReviewDomain;
import com.desofs.backend.domain.entities.PaymentEntity;
import com.desofs.backend.exceptions.DatabaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReviewRepository {

    private final ReviewRepositoryJPA reviewRepositoryJPA;
    private final ImageRepositoryJpa imageRepositoryJpa;
    private final ReviewMapper mapper;

    public void create(ReviewDomain reviewDomain) throws DatabaseException {
        ReviewDB reviewDB = this.reviewRepositoryJPA.findById(reviewDomain.getId().value()).orElse(null);

        if (reviewDB != null) {
            throw new DatabaseException("Duplicated ID violation.");
        }

        this.reviewRepositoryJPA.save(this.mapper.domainToDb(reviewDomain));
    }

    public ReviewDomain findById(String reviewId) {
        try {
            ReviewDB reviewDB = this.reviewRepositoryJPA.findById(reviewId).orElse(null);
            if (reviewDB != null) {
                List<ImageUrlDB> imageUrlDB = imageRepositoryJpa.findByReviewId(reviewId);
                return this.mapper.dbToDomain(reviewDB, imageUrlDB);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public ReviewDomain findByBookingId(String bookingId) {
        ReviewDB reviewDB = this.reviewRepositoryJPA.findByBookingId(bookingId);
        return this.findById(reviewDB.getId());
    }

}
