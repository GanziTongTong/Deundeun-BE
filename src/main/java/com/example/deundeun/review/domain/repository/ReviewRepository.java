package com.example.deundeun.review.domain.repository;

import com.example.deundeun.review.domain.Review;
import com.example.deundeun.store.domain.Store;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("""
            SELECT DISTINCT r FROM Review r 
            LEFT JOIN FETCH r.reviewImages 
            LEFT JOIN FETCH r.keywords 
            WHERE r.store = :store""")
    List<Review> findAllByStoreWithImages(Store store);
}
