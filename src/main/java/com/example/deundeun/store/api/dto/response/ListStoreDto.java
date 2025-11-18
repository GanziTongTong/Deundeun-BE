package com.example.deundeun.store.api.dto.response;

import java.util.List;

public record ListStoreDto(
        List<StoreInfoDto> stores
) {
}
