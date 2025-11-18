package com.example.deundeun.store.api.dto.request;

public record StoreDto(
        double user_latitude,
        double user_longitude,
        double radiusKm
) {
}
