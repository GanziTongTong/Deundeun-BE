package com.example.deundeun.store.domin;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "store")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String facltNm;

    @Column(nullable = false)
    private String roadnmAddr;

    private String sigunNm;
    private String lotnoAddr;
    private String logt; // 경도
    private String lat; // 위도

    @Enumerated(EnumType.STRING)
    private Category category;

    private String phoneNumber;

    @Column(length = 512)
    private String openingHours;

    @Builder
    public Store(String sigunNm, String facltNm, String lotnoAddr, String roadnmAddr, String logt, String lat, Category category) {
        this.sigunNm = sigunNm;
        this.facltNm = facltNm;
        this.lotnoAddr = lotnoAddr;
        this.roadnmAddr = roadnmAddr;
        this.logt = logt;
        this.lat = lat;
        this.category = category;
    }
}