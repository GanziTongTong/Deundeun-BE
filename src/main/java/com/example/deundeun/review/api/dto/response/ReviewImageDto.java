package com.example.deundeun.review.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "리뷰 이미지 정보 응답 DTO")
public record ReviewImageDto(
        @Schema(description = "리뷰 이미지 ID", example = "1")
        Long imageId,

        @Schema(description = "이미지 URL", example = "https://~~.jpg")
        String imageUrl
) {
    public static ReviewImageDto from(com.example.deundeun.review.domain.ReviewImage image) {
        return new ReviewImageDto(
                image.getId(),
                image.getImgUrl()
        );
    }
}