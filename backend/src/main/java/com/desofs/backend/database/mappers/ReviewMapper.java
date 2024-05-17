package com.desofs.backend.database.mappers;

import com.desofs.backend.domain.aggregates.ReviewDomain;
import com.desofs.backend.domain.valueobjects.ImageUrl;
import com.desofs.backend.dtos.ReviewDto;

public class ReviewMapper {

    public ReviewDto domainToDto(ReviewDomain review) {
        return new ReviewDto(
                review.getId().value(),
                review.getAuthorId().value(),
                review.getPropertyId().value(),
                review.getText().value(),
                review.getStars().getStars(),
                review.isBanned(),
                review.getImageUrlList().stream().map(ImageUrl::getUrl).toList());
    }
}