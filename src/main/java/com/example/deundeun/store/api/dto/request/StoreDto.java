package com.example.deundeun.store.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "가맹점 조회 요청 DTO")
public record StoreDto(
        @Schema(description = "사용자 위도", example = "37.55315", required = true)
        double user_latitude,

        @Schema(description = "사용자 경도", example = "127.0240298256", required = true)
        double user_longitude,

        @Schema(description = "검색 반경 (km). 미입력시 기본값 5km", example = "5.0")
        Double radiusKm  // Double로 변경하여 null 허용
) {
    // 기본값 5km 설정
    public double getRadiusKm() {
        return radiusKm != null ? radiusKm : 5.0;
    }
}
