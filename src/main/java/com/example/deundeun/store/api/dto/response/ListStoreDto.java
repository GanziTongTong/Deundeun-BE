package com.example.deundeun.store.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "가맹점 목록 응답 DTO")
public record ListStoreDto(
        @Schema(description = "가맹점 정보 목록")
        List<StoreDistanceDto> stores
) {
}
