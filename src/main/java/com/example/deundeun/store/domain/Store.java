package com.example.deundeun.store.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
    name = "store",
        uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_store_name_address_category",
            columnNames = {"faclt_nm", "roadnm_addr", "categories"}
        )
    },
    indexes = {
        @Index(name = "idx_lat_logt", columnList = "lat, logt"),
        @Index(name = "idx_categories", columnList = "categories")
    }
)
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String facltNm;

    @Column(nullable = false, length = 500)
    private String roadnmAddr;

    private String sigunNm;
    private String lotnoAddr;
    private String logt; // 경도
    private String lat; // 위도

    // 카테고리를 콤마로 구분된 문자열로 저장 (예: "CHILD_MEAL_CARD,GOOD_NEIGHBOR_STORE")
    @Column(length = 500)
    private String categories;

    private String phoneNumber;

    @Column(length = 512)
    private String openingHours;

    @Builder
    public Store(String sigunNm, String facltNm, String lotnoAddr, String roadnmAddr, String logt, String lat, String categories) {
        this.sigunNm = sigunNm;
        this.facltNm = facltNm;
        this.lotnoAddr = lotnoAddr;
        this.roadnmAddr = roadnmAddr;
        this.logt = logt;
        this.lat = lat;
        this.categories = categories;
    }

    // 카테고리 리스트로 반환
    public List<Category> getCategoryList() {
        List<Category> categoryList = new ArrayList<>();
        if (categories != null && !categories.isEmpty()) {
            String[] categoryArray = categories.split(",");
            for (String cat : categoryArray) {
                try {
                    categoryList.add(Category.valueOf(cat.trim()));
                } catch (IllegalArgumentException e) {
                    // 잘못된 카테고리는 무시
                }
            }
        }
        return categoryList;
    }
}