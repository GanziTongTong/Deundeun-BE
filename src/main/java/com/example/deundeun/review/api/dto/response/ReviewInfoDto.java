package com.example.deundeun.review.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "리뷰 상세 정보 응답 DTO")
public record ReviewInfoDto(
        @Schema(description = "리뷰 ID", example = "1")
        Long reviewId,

        @Schema(description = "리뷰 이미지 목록")
        List<ReviewImageDto> images,

        @Schema(description = "리뷰 키워드 목록")
        ReviewKeywordDto keywords
) {
    public static ReviewInfoDto of(Long reviewId,
                                   List<ReviewImageDto> images,
                                   ReviewKeywordDto keywords) {
        return new ReviewInfoDto(reviewId, images, keywords);
    }
}