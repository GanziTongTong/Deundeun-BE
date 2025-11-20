package com.example.deundeun.store.api.dto.response;

import com.example.deundeun.review.api.dto.response.ReviewInfoDto;
import com.example.deundeun.store.domin.Category;
import com.example.deundeun.store.domin.Store;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "가맹점 상세 정보 DTO")
public record StoreInfoDto(
        @Schema(description = "가맹점 ID", example = "1")
        Long storeId,

        @Schema(description = "가맹점 이름", example = "동동빵집")
        String name,

        @Schema(description = "카테고리 목록", example = "[\"CHILD_MEAL_CARD\", \"GOOD_NEIGHBOR_STORE\"]")
        List<Category> categories,

        @Schema(description = "도로명 주소", example = "경기도 수원시 권선구 덕영대로1217번길 25-4")
        String address,

        @Schema(description = "전화번호", example = "010-1234-5678")
        String phoneNumber,

        @Schema(description = "영업시간", example = "09:00-21:00")
        String openingHours,

        @Schema(description = "리뷰")
        List<ReviewInfoDto> reviews
        @Schema(description = "위도", example = "37.55315")
        Double latitude,

        @Schema(description = "경도", example = "127.0240298256")
        Double longitude
) {
    public static StoreInfoDto of(Store store, List<ReviewInfoDto> reviews) {
        return new StoreInfoDto(
                store.getId(),
                store.getFacltNm(),
                store.getCategoryList(),
                store.getRoadnmAddr(),
                store.getPhoneNumber(),
                store.getOpeningHours(),
                reviews
        );
                parseDoubleOrNull(store.getLat()),
                parseDoubleOrNull(store.getLogt())
        );
    }

    private static Double parseDoubleOrNull(String s) {
        if (s == null) return null;
        try {
            return Double.parseDouble(s.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
