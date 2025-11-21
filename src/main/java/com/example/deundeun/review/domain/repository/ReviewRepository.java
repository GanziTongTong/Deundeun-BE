package com.example.deundeun.review.domain.repository;

import com.example.deundeun.review.domain.Review;
import com.example.deundeun.store.domain.Store;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByStore(Store store);
}
