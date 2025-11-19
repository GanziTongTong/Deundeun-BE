package com.example.deundeun.review.api.dto.request;

import com.example.deundeun.review.domain.ReviewKeyword;
import java.util.Set;

public record ReviewDto(
        Set<ReviewKeyword> keywords
) {
}
