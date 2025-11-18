package com.example.deundeun.store.api.dto.response;

import com.example.deundeun.store.domin.Category;
import com.example.deundeun.store.domin.Store;

public record StoreInfoDto(
        Long storeId,
        String name,
        Category category,
        String phoneNumber,
        String openingHours
) {
    public static StoreInfoDto from(Store store) {
        return new StoreInfoDto(
                store.getId(),
                store.getFacltNm(),
                store.getCategory(),
                store.getPhoneNumber(),
                store.getOpeningHours());
    }
}
