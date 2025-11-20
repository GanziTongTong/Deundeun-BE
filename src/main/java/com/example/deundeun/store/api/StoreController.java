package com.example.deundeun.store.api;

import com.example.deundeun.store.api.dto.request.StoreDto;
import com.example.deundeun.store.api.dto.response.ListStoreDto;
import com.example.deundeun.store.api.dto.response.StoreInfoDto;
import com.example.deundeun.store.application.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/store")
@Tag(name = "가맹점 API", description = "가맹점 조회 및 검색 API")
public class StoreController {

    private final StoreService storeService;

    @Operation(
            summary = "근처 가맹점 조회",
            description = "사용자의 위도/경도를 기반으로 반경 내 가맹점을 조회합니다.\n\n" +
                    "- radiusKm 기본값: 5km\n- 거리순 정렬",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "사용자 위치 정보 및 검색 반경",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = StoreDto.class),
                            mediaType = "application/json"
                    ))
    )
    @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = ListStoreDto.class))
    )
    @PostMapping
    public ResponseEntity<ListStoreDto> getAllStore(@RequestBody StoreDto storeDto) {
        ListStoreDto listStoreDto = storeService.findNearbyStores(
                storeDto.user_latitude(),
                storeDto.user_longitude(),
                storeDto.getRadiusKm()
        );
        return ResponseEntity.ok(listStoreDto);
    }

    @Operation(
            summary = "가맹점 상세조회",
            description = "가맹점 ID로 상세 정보를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "400", description = "가맹점을 찾을 수 없습니다")
            }
    )
    @GetMapping("/detail")
    public ResponseEntity<StoreInfoDto> getStoreDetail(
            @Parameter(description = "조회할 가맹점 ID", required = true, example = "1")
            @RequestParam Long storeId) {
        return ResponseEntity.ok(storeService.getStoreDetail(storeId));
    }

    @Operation(
            summary = "가맹점 이름 검색",
            description = "가맹점 이름으로 검색하고 사용자 위치 기반 반경 내 결과를 반환합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "사용자 위치 정보 및 검색 반경",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = StoreDto.class),
                            mediaType = "application/json"
                    ))
    )
    @PostMapping("/search")
    public ResponseEntity<ListStoreDto> searchStoresByName(
            @RequestBody StoreDto storeDto,
            @Parameter(description = "검색할 가맹점 이름", required = true, example = "해물")
            @RequestParam String keyword) {

        return ResponseEntity.ok(storeService.searchStoresByName(
                storeDto.user_latitude(),
                storeDto.user_longitude(),
                storeDto.getRadiusKm(),
                keyword
        ));
    }

    @Operation(
            summary = "[테스트] 전체 가맹점 수 조회",
            description = "DB에 저장된 전체 가맹점 수를 반환합니다.",
            responses = @ApiResponse(responseCode = "200", description = "조회 성공")
    )
    @GetMapping("/count")
    public ResponseEntity<Long> getStoreCount() {
        return ResponseEntity.ok(storeService.getStoreCount());
    }
}
