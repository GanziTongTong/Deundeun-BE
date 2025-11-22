package com.example.deundeun.store.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private String facltNm; // 가게명

    @Column(nullable = false, length = 500)
    private String roadnmAddr;

    private String lotnoAddr;
    private String logt; // 경도
    private String lat; // 위도

    @Column(length = 500)
    private String categories;

    private String phoneNumber;

    @Column(length = 512)
    private String openingHours;

    @Builder
    private Store(String facltNm, String lotnoAddr, String roadnmAddr, String logt, String lat, String categories) {
        this.facltNm = facltNm;
        this.lotnoAddr = lotnoAddr;
        this.roadnmAddr = roadnmAddr;
        this.logt = logt;
        this.lat = lat;
        this.categories = categories;
    }

    public List<Category> getCategoryList() {
        List<Category> categoryList = new ArrayList<>();
        if (categories != null && !categories.isEmpty()) {
            String[] categoryArray = categories.split(",");
            for (String cat : categoryArray) {
                try {
                    categoryList.add(Category.valueOf(cat.trim()));
                } catch (IllegalArgumentException e) {
                }
            }
        }
        return categoryList;
    }
}