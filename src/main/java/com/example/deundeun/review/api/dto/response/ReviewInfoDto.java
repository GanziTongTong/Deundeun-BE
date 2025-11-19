package com.example.deundeun.review.api.dto.response;

import com.example.deundeun.review.domain.ReviewImage;
import com.example.deundeun.review.domain.ReviewKeyword;
import java.util.List;
import java.util.Set;

public record ReviewInfoDto(
        Long reviewId,
        List<ReviewImage> images,
        Set<ReviewKeyword> keywords
) {
    public static ReviewInfoDto from(Long reviewId, List<ReviewImage> images, Set<ReviewKeyword> keywords) {
        return new ReviewInfoDto(reviewId, images, keywords);
    }
}
