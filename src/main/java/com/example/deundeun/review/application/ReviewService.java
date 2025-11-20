package com.example.deundeun.review.application;

import com.example.deundeun.review.api.dto.request.ReviewDto;
import com.example.deundeun.review.domain.Review;
import com.example.deundeun.review.domain.ReviewImage;
import com.example.deundeun.review.domain.repository.ReviewImageRepository;
import com.example.deundeun.review.domain.repository.ReviewRepository;
import com.example.deundeun.store.domin.Store;
import com.example.deundeun.store.domin.repository.StoreRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final S3Uploader s3Uploader;
    private final StoreRepository storeRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;

    @Transactional
    public void createReview(Long storeId,
                             ReviewDto request,
                             List<MultipartFile> images) {

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 가게가 존재하지 않습니다."));

        Review review = reviewRepository.save(
                Review.builder()
                        .keywords(request.keywords())
                        .store(store)
                        .build()
        );

        if (images != null && !images.isEmpty()) {
            for (MultipartFile image : images) {
                String url = s3Uploader.upload(image, "review");

                reviewImageRepository.save(
                        ReviewImage.builder()
                                .review(review)
                                .imgUrl(url)
                                .build()
                );
            }
        }
    }
}
