package com.example.deundeun.store.domain.repository;

public interface StoreWithDistance {
    Long getId();
    String getFacltNm();
    String getRoadnmAddr();
    String getLotnoAddr();
    String getLogt();
    String getLat();
    String getCategories();
    String getPhoneNumber();
    String getOpeningHours();
    Double getDistance();
}