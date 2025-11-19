package com.example.deundeun.review.api.dto.response;

import java.util.List;

public record ReviewListDto(
        Long storeId,
        List<ReviewInfoDto> reviews
) {
}
