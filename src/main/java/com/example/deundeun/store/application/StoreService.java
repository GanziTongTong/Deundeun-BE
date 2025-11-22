package com.example.deundeun.store.application;

import com.example.deundeun.review.api.dto.response.ReviewImageDto;
import com.example.deundeun.review.api.dto.response.ReviewInfoDto;
import com.example.deundeun.review.api.dto.response.ReviewKeywordDto;
import com.example.deundeun.review.domain.Review;
import com.example.deundeun.review.domain.repository.ReviewRepository;
import com.example.deundeun.store.api.dto.response.ListStoreDto;
import com.example.deundeun.store.api.dto.response.StoreDistanceDto;
import com.example.deundeun.store.api.dto.response.StoreInfoDto;
import com.example.deundeun.store.domain.Category;
import com.example.deundeun.store.domain.Store;
import com.example.deundeun.store.domain.repository.StoreRepository;
import com.example.deundeun.store.domain.repository.StoreWithDistance;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

    private final StoreRepository storeRepository;
    private final ReviewRepository reviewRepository;

    /**
     * 반경 내 전체 가맹점 조회
     */
    public ListStoreDto findNearbyStores(
            double userLatitude,
            double userLongitude,
            double radiusKm,
            Category category
    ) {
        List<StoreWithDistance> stores;

        if (category != null) {
            stores = storeRepository.findStoresWithinDistanceByCategory(
                    userLatitude, userLongitude, radiusKm, category.name());
        } else {
            stores = storeRepository.findStoresWithinDistance(
                    userLatitude, userLongitude, radiusKm);
        }

        return convertToDistanceDto(stores);
    }

    /**
     * 가맹점 상세조회
     */
    public StoreInfoDto getStoreDetail(Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(() ->
                new IllegalArgumentException("가맹점을 찾을 수 없습니다."));

        List<Review> reviews = reviewRepository.findAllByStoreWithImages(store);

        List<ReviewInfoDto> reviewInfoDtos = reviews.stream()
                .map(review -> {
                    List<ReviewImageDto> imageDtos = review.getReviewImages().stream()
                            .map(ReviewImageDto::from)
                            .toList();

                    ReviewKeywordDto keywordDto = new ReviewKeywordDto(
                            review.getKeywords().stream()
                                    .map(Enum::name)
                                    .collect(java.util.stream.Collectors.toSet())
                    );

                    return ReviewInfoDto.of(review.getId(), imageDtos, keywordDto);
                })
                .toList();

        return StoreInfoDto.of(store, reviewInfoDtos);
    }

    // 가게명 검색
    public ListStoreDto searchStoresByName(
            double userLatitude,
            double userLongitude,
            double radiusKm,
            String keyword,
            com.example.deundeun.store.domain.Category category
    ) {
        List<StoreWithDistance> stores;

        if (category != null) {
            stores = storeRepository.findStoresWithinDistanceByCategoryAndName(
                    userLatitude, userLongitude, radiusKm, category.name(), keyword);
        } else {
            stores = storeRepository.findStoresWithinDistanceByName(
                    userLatitude, userLongitude, radiusKm, keyword);
        }

        return convertToDistanceDto(stores);
    }

    private ListStoreDto convertToDistanceDto(List<StoreWithDistance> stores) {
        List<StoreDistanceDto> storeDistanceDtos = stores.stream()
                .map(StoreDistanceDto::from)
                .collect(Collectors.toList());

        return new ListStoreDto(storeDistanceDtos);
    }
}