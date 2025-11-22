package com.example.deundeun.store.api.dto.response;

import com.example.deundeun.store.application.StoreTimeFormatter;
import com.example.deundeun.store.domain.Category;
import com.example.deundeun.store.domain.repository.StoreWithDistance; // 인터페이스 import 필수
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    @Schema(description = "카테고리 목록", example = "[\"CHILD_MEAL_CARD\", \"GOOD_NEIGHBOR_STORE\"]")
    private List<Category> categories;

    @Schema(description = "사용자로부터의 거리 (km)", example = "2.5")
    private double distance;

    @Schema(description = "전화번호", example = "031-123-4567")
    private String phoneNumber;

    @Schema(description = "영업시간", example = "09:00-21:00")
    private String openingHours;

    @Schema(description = "위도", example = "37.55315")
    private Double latitude;

    @Schema(description = "경도", example = "127.0240298256")
    private Double longitude;

    public static StoreDistanceDto from(StoreWithDistance store) {
        return new StoreDistanceDto(
                store.getId(),
                store.getFacltNm(),
                store.getRoadnmAddr(),
                parseCategories(store.getCategories()),
                store.getDistance(),
                store.getPhoneNumber(),
                StoreTimeFormatter.formatOpeningHours(store.getOpeningHours()),
                parseDoubleOrNull(store.getLat()),
                parseDoubleOrNull(store.getLogt())
        );
    }

    private static List<Category> parseCategories(String categoryStr) {
        if (categoryStr == null || categoryStr.isBlank()) {
            return new ArrayList<>();
        }
        try {
            return Arrays.stream(categoryStr.split(","))
                    .map(String::trim)
                    .map(Category::valueOf)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            return Collections.emptyList();
        }
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