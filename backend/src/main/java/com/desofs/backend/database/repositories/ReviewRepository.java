package com.desofs.backend.database.repositories;

import com.desofs.backend.database.mappers.ImageUrlMapper;
import com.desofs.backend.database.mappers.ReviewMapper;
import com.desofs.backend.database.models.ImageUrlDB;
import com.desofs.backend.database.models.ReviewDB;
import com.desofs.backend.database.springRepositories.ImageRepositoryJPA;
import com.desofs.backend.database.springRepositories.ReviewRepositoryJPA;
import com.desofs.backend.domain.aggregates.ReviewDomain;
import com.desofs.backend.domain.valueobjects.ImageUrl;
import com.desofs.backend.exceptions.DatabaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("ReviewRepositoryCapsule")
@RequiredArgsConstructor
public class ReviewRepository {

    private final ReviewRepositoryJPA reviewRepositoryJPA;

    private final ImageRepositoryJPA imageRepositoryJpa;

    private final ReviewMapper reviewMapper;

    private final ImageUrlMapper imageUrlMapper;

    public void create(ReviewDomain reviewDomain) throws DatabaseException {
        ReviewDB reviewDB = this.reviewRepositoryJPA.findById(reviewDomain.getId().value()).orElse(null);

        if (reviewDB != null) {
            throw new DatabaseException("Duplicated ID violation.");
        }

        this.reviewRepositoryJPA.save(this.reviewMapper.domainToDb(reviewDomain));

        for (ImageUrl imageToSave : reviewDomain.getImageUrlList()) {
            this.imageRepositoryJpa.save(imageUrlMapper.domainToDb(imageToSave, reviewDomain.getId().value()));
        }
    }

    public ReviewDomain findById(String reviewId) {
        try {
            ReviewDB reviewDB = this.reviewRepositoryJPA.findById(reviewId).orElse(null);
            if (reviewDB != null) {
                List<ImageUrlDB> imageUrlDB = imageRepositoryJpa.findByReviewId(reviewId);
                return this.reviewMapper.dbToDomain(reviewDB, imageUrlDB);
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
