package com.example.deundeun.review.domain;

import lombok.Getter;

@Getter
public enum ReviewKeyword {
    TASTY("맛있어요"),
    FRESH_INGREDIENTS("재료가 신선해요"),
    KIND_SERVICE("서비스가 친절해요"),
    OWNER_WELCOMING("사장님께서 반겨주셨어요"),
    GENEROUS_PORTION("양이 많아요"),
    GOOD_FOR_SOLO("혼밥하기 좋아요");

    private final String description;

    ReviewKeyword(String description) {
        this.description = description;
    }
}