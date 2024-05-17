package com.desofs.backend.domain.aggregates;

import com.desofs.backend.domain.valueobjects.*;
import com.desofs.backend.dtos.CreateReviewDto;
import com.desofs.backend.utils.ListUtils;

import java.util.List;
import java.util.UUID;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

public class ReviewDomain {
    private final Id id;
    private final Id authorId;
    private final Id bookingId;
    private final ReviewText text;
    private final ReviewStars stars;
    private boolean banned;
    private final List<ImageUrl> imageUrlList;

    // Constructors ----------------------------------------------------------------------------------------------------

    public ReviewDomain(Id id, Id authorId, Id bookingId, ReviewText text, ReviewStars stars, boolean banned,
                        List<ImageUrl> imageUrlList) {
        notNull(id, "Id must not be null.");
        notNull(authorId, "AuthorId must not be null.");
        notNull(bookingId, "BookingId must not be null.");
        notNull(text, "Text must not be null.");
        notNull(stars, "Stars must not be null.");
        notNull(imageUrlList, "ImageUrlList must not be null.");
        isTrue(!ListUtils.hasDuplicates(imageUrlList), "ImageUrlList cannot contain duplicates.");

        this.id = id.copy();
        this.authorId = authorId.copy();
        this.bookingId = bookingId.copy();
        this.text = text.copy();
        this.stars = stars.copy();
        this.banned = banned;
        this.imageUrlList = List.copyOf(imageUrlList);
    }

    public ReviewDomain(CreateReviewDto reviewDto) {
        this(Id.create(UUID.randomUUID().toString()), Id.create(reviewDto.getAuthorId()), Id.create(reviewDto.getBookingId()),
                ReviewText.create(reviewDto.getText()), ReviewStars.create(reviewDto.getStars()),
                reviewDto.isBanned(), reviewDto.getImageUrlList().stream().map(ImageUrl::create).toList());
    }

    private static boolean imgUrlListIsValid(List<ImageUrl> imageUrlList) {
        return ListUtils.hasDuplicates(imageUrlList);
    }

    // Getters ---------------------------------------------------------------------------------------------------------

    public Id getId() {
        return id.copy();
    }

    public Id getAuthorId() {
        return authorId.copy();
    }

    public Id getBookingId() {
        return bookingId.copy();
    }

    public ReviewText getText() {
        return text.copy();
    }

    public ReviewStars getStars() {
        return stars.copy();
    }

    public boolean isBanned() {
        return banned;
    }

    public List<ImageUrl> getImageUrlList() {
        return List.copyOf(imageUrlList);
    }

    public ReviewDomain copy() {
        return new ReviewDomain(id.copy(), authorId.copy(), bookingId.copy(), text.copy(),
                stars.copy(), banned, List.copyOf(imageUrlList));
    }

    // Domain methods --------------------------------------------------------------------------------------------------

    public void banReview() {
        if (this.banned) {
            throw new IllegalArgumentException("The review is already banned");
        }
        this.banned = true;
    }
}
