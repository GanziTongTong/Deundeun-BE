package com.example.deundeun.review.domain.repository;

import com.example.deundeun.review.domain.Review;
import com.example.deundeun.review.domain.ReviewImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
    List<ReviewImage> findByReview(Review review);
}
