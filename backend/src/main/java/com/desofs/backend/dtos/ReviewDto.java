package com.desofs.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ReviewDto {

    private final String id;
    private final String authorId;
    private final String propertyId;
    private final String text;
    private final int stars;
    private final boolean banned;
    private final List<String> imageUrlList;

}
