package com.example.deundeun.store.api.dto.response;

import com.example.deundeun.store.domin.Category;
import com.example.deundeun.store.domin.Store;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreDistanceDto {
    private Long storeId;
    private String name;
    private String address;
    private Category category;
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