package com.example.deundeun.review.api.dto.request;

import com.example.deundeun.review.domain.ReviewKeyword;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;

@Schema(description = "리뷰 생성 요청 DTO")
public record ReviewDto(
        @Schema(description = "리뷰 키워드", example = "[\"TASTY\", \"FRESH_INGREDIENTS\"]")
        Set<ReviewKeyword> keywords
) {
}
