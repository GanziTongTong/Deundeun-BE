package com.example.deundeun.review.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;

@Schema(description = "리뷰 키워드 정보 응답 DTO")
public record ReviewKeywordDto(
        @Schema(description = "리뷰 키워드 등록",
        example = "[\"TASTY\", \"FRESH_INGREDIENTS\"]")
        Set<String> keywords
) {
}