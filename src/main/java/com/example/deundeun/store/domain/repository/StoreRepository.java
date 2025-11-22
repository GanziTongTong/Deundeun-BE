package com.example.deundeun.store.domain.repository;

import com.example.deundeun.store.domain.Store;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StoreRepository extends JpaRepository<Store, Long> {

    // 1. 반경 내 조회
    @Query(value = """
            SELECT s.id, s.faclt_nm, s.roadnm_addr, s.lotno_addr, 
                   s.logt, s.lat, s.categories, s.phone_number, s.opening_hours,
                   (6371 * acos(
                        cos(radians(:latitude)) * cos(radians(CAST(s.lat AS DOUBLE))) *
                        cos(radians(CAST(s.logt AS DOUBLE)) - radians(:longitude)) +
                        sin(radians(:latitude)) * sin(radians(CAST(s.lat AS DOUBLE)))
                   )) AS distance
            FROM store s
            WHERE (6371 * acos(
                        cos(radians(:latitude)) * cos(radians(CAST(s.lat AS DOUBLE))) *
                        cos(radians(CAST(s.logt AS DOUBLE)) - radians(:longitude)) +
                        sin(radians(:latitude)) * sin(radians(CAST(s.lat AS DOUBLE)))
                   )) <= :distance
            ORDER BY distance ASC
            """, nativeQuery = true)
    List<StoreWithDistance> findStoresWithinDistance(
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("distance") double distance
    );

    // 2. 가게명 검색 + 반경 내 조회
    @Query(value = """
            SELECT s.id, s.faclt_nm, s.roadnm_addr, s.lotno_addr, 
                   s.logt, s.lat, s.categories, s.phone_number, s.opening_hours,
                   (6371 * acos(
                        cos(radians(:latitude)) * cos(radians(CAST(s.lat AS DOUBLE))) *
                        cos(radians(CAST(s.logt AS DOUBLE)) - radians(:longitude)) +
                        sin(radians(:latitude)) * sin(radians(CAST(s.lat AS DOUBLE)))
                   )) AS distance
            FROM store s
            WHERE s.faclt_nm LIKE CONCAT('%', :keyword, '%')
              AND (6371 * acos(
                        cos(radians(:latitude)) * cos(radians(CAST(s.lat AS DOUBLE))) *
                        cos(radians(CAST(s.logt AS DOUBLE)) - radians(:longitude)) +
                        sin(radians(:latitude)) * sin(radians(CAST(s.lat AS DOUBLE)))
                   )) <= :radiusKm
            ORDER BY distance ASC
            """, nativeQuery = true)
    List<StoreWithDistance> findStoresWithinDistanceByName(
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("radiusKm") double radiusKm,
            @Param("keyword") String keyword
    );

    // 3. 카테고리 필터 + 반경 내 조회
    @Query(value = """
            SELECT s.id, s.faclt_nm, s.roadnm_addr, s.lotno_addr, 
                   s.logt, s.lat, s.categories, s.phone_number, s.opening_hours,
                   (6371 * acos(
                        cos(radians(:latitude)) * cos(radians(CAST(s.lat AS DOUBLE))) *
                        cos(radians(CAST(s.logt AS DOUBLE)) - radians(:longitude)) +
                        sin(radians(:latitude)) * sin(radians(CAST(s.lat AS DOUBLE)))
                   )) AS distance
            FROM store s
            WHERE s.categories LIKE CONCAT('%', :category, '%')
              AND (6371 * acos(
                        cos(radians(:latitude)) * cos(radians(CAST(s.lat AS DOUBLE))) *
                        cos(radians(CAST(s.logt AS DOUBLE)) - radians(:longitude)) +
                        sin(radians(:latitude)) * sin(radians(CAST(s.lat AS DOUBLE)))
                   )) <= :distance
            ORDER BY distance ASC
            """, nativeQuery = true)
    List<StoreWithDistance> findStoresWithinDistanceByCategory(
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("distance") double distance,
            @Param("category") String category
    );

    // 4. 카테고리 필터 + 가게명 검색 + 반경 내 조회
    @Query(value = """
            SELECT s.id, s.faclt_nm, s.roadnm_addr, s.lotno_addr, 
                   s.logt, s.lat, s.categories, s.phone_number, s.opening_hours,
                   (6371 * acos(
                        cos(radians(:latitude)) * cos(radians(CAST(s.lat AS DOUBLE))) *
                        cos(radians(CAST(s.logt AS DOUBLE)) - radians(:longitude)) +
                        sin(radians(:latitude)) * sin(radians(CAST(s.lat AS DOUBLE)))
                   )) AS distance
            FROM store s
            WHERE s.categories LIKE CONCAT('%', :category, '%')
              AND s.faclt_nm LIKE CONCAT('%', :keyword, '%')
              AND (6371 * acos(
                        cos(radians(:latitude)) * cos(radians(CAST(s.lat AS DOUBLE))) *
                        cos(radians(CAST(s.logt AS DOUBLE)) - radians(:longitude)) +
                        sin(radians(:latitude)) * sin(radians(CAST(s.lat AS DOUBLE)))
                   )) <= :radiusKm
            ORDER BY distance ASC
            """, nativeQuery = true)
    List<StoreWithDistance> findStoresWithinDistanceByCategoryAndName(
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("radiusKm") double radiusKm,
            @Param("category") String category,
            @Param("keyword") String keyword
    );
}