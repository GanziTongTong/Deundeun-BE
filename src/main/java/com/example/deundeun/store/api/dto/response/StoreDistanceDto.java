package com.example.deundeun.store.api.dto.response;

import com.example.deundeun.store.domin.Category;
import com.example.deundeun.store.domin.Store;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "거리 포함 가맹점 정보 DTO")
public class StoreDistanceDto {
    @Schema(description = "가맹점 ID", example = "1")
    private Long storeId;

    @Schema(description = "가맹점 이름", example = "동동빵집")
    private String name;

    @Schema(description = "도로명 주소", example = "경기도 수원시 권선구 덕영대로1217번길 25-4")
    private String address;

    @Schema(description = "카테고리", example = "CHILD_MEAL_CARD")
    private Category category;

    @Schema(description = "사용자로부터의 거리 (km)", example = "2.5")
    private double distance;

    public static StoreDistanceDto of(Store store, double distance) {
        return new StoreDistanceDto(
                store.getId(),
                store.getFacltNm(),
                store.getRoadnmAddr(),
                store.getCategory(),
                distance
        );
    }
}