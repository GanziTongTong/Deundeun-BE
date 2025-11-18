package com.example.deundeun.store.application;

import com.example.deundeun.store.api.dto.response.StoreDistanceDto;
import com.example.deundeun.store.api.dto.response.StoreInfoDto;
import com.example.deundeun.store.domin.Store;
import com.example.deundeun.store.domin.repository.StoreRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    /**
     * 반경 내 전체 가맹점 조회 (카테고리 필터 선택 가능)
     */
    public List<StoreDistanceDto> findNearbyStores(
            double userLatitude,
            double userLongitude,
            double radiusKm,
            com.example.deundeun.store.domin.Category category
    ) {
        List<Store> stores;

        if (category != null) {
            // 카테고리 필터가 있는 경우
            stores = storeRepository.findStoresWithinDistanceByCategory(
                    userLatitude, userLongitude, radiusKm, category.name());
        } else {
            // 카테고리 필터가 없는 경우 전체 조회
            stores = storeRepository.findStoresWithinDistance(
                    userLatitude, userLongitude, radiusKm);
        }

        return convertToDistanceDto(userLatitude, userLongitude, stores);
    }

    /**
     * 가맹점 상세조회
     */
    public StoreInfoDto getStoreDetail(Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(() ->
                new IllegalArgumentException("가맹점을 찾을 수 없습니다."));
        return StoreInfoDto.from(store);
    }

    /**
     * 전체 가맹점 수 조회 (테스트용)
     */
    public Long getStoreCount() {
        return storeRepository.count();
    }

    /**
     * 가게명 검색 + 반경 내 조회 (카테고리 필터 선택 가능)
     */
    public List<StoreDistanceDto> searchStoresByName(
            double userLatitude,
            double userLongitude,
            double radiusKm,
            String keyword,
            com.example.deundeun.store.domin.Category category
    ) {
        List<Store> stores;

        if (category != null) {
            // 카테고리 필터가 있는 경우
            stores = storeRepository.findStoresWithinDistanceByCategoryAndName(
                    userLatitude, userLongitude, radiusKm, category.name(), keyword);
        } else {
            // 카테고리 필터가 없는 경우
            stores = storeRepository.findStoresWithinDistanceByName(
                    userLatitude, userLongitude, radiusKm, keyword);
        }

        return convertToDistanceDto(userLatitude, userLongitude, stores);
    }

    private List<StoreDistanceDto> convertToDistanceDto(
            double userLatitude,
            double userLongitude,
            List<Store> stores
    ) {
        return stores.stream()
                .map(store -> {
                    double distance = calculateDistance(userLatitude, userLongitude, Double.parseDouble(store.getLat()),
                            Double.parseDouble(store.getLogt()));
                    return StoreDistanceDto.of(store, distance);
                })
                .collect(Collectors.toList());
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS = 6371;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }
}
