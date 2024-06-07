package com.desofs.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@AllArgsConstructor
public class CreateReviewDto {
    private final String bookingId;
    private final String text;
    private final int stars;
    private final List<MultipartFile> imageUrlList;
}
