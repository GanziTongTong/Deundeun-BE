package com.example.deundeun.store.api.dto.request;

import com.example.deundeun.store.domain.Category;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "가맹점 조회 요청 DTO")
public record StoreDto(
        @Schema(description = "사용자 위도", example = "37.2789743114", required = true)
        double user_latitude,

        @Schema(description = "사용자 경도", example = "127.0302775391", required = true)
        double user_longitude,

        @Schema(description = "검색 반경 (km). 미입력시 기본값 5km", example = "5.0")
        Double radiusKm,

        @Schema(description = "카테고리 필터 (선택). 미입력시 전체 조회",
                example = "CHILD_MEAL_CARD",
                allowableValues = {"CHILD_MEAL_CARD", "GOOD_INFLUENCE_STORE", "GOOD_NEIGHBOR_STORE"})
        Category category
) {
    public double getRadiusKm() {
        return radiusKm != null ? radiusKm : 5.0;
    }
}
