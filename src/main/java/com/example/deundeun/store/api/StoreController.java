package com.example.deundeun.store.api;

import com.example.deundeun.store.api.dto.request.StoreDto;
import com.example.deundeun.store.api.dto.response.StoreDistanceDto;
import com.example.deundeun.store.api.dto.response.StoreInfoDto;
import com.example.deundeun.store.application.StoreService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/store")
public class StoreController {

    private final StoreService storeService;

    // 근처 가맹점 조회
    @GetMapping
    public ResponseEntity<List<StoreDistanceDto>> getAllStore(@RequestBody StoreDto storeDto) {
        List<StoreDistanceDto> listStoreDto = storeService.findNearbyStores(storeDto.user_latitude(),
                storeDto.user_longitude(), storeDto.radiusKm());
        return ResponseEntity.ok(listStoreDto);
    }

    // 가맹점 상세조회
    @GetMapping("/detail")
    public ResponseEntity<StoreInfoDto> getStoreDetail(@RequestParam Long storeId) {
        return ResponseEntity.ok(storeService.getStoreDetail(storeId));
    }

    // 가맹점 검색
    @GetMapping("/search")
    public ResponseEntity<List<StoreDistanceDto>> searchStoresByName(@RequestBody StoreDto storeDto,
                                                                     @RequestParam(value = "keyword") String keyword) {
        return ResponseEntity.ok(storeService.searchStoresByName(
                storeDto.user_latitude(),
                storeDto.user_longitude(),
                storeDto.radiusKm(),
                keyword
        ));
    }
}
