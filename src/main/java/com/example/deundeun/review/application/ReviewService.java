package com.example.deundeun.review.application;

import com.example.deundeun.review.api.dto.request.ReviewDto;
import com.example.deundeun.review.api.dto.response.ReceiptInfoDto;
import com.example.deundeun.review.api.dto.response.ReviewInfoDto;
import com.example.deundeun.review.api.dto.response.ReviewListDto;
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
    private final UpstageService upstageService;

    @Transactional
    public void createReview(Long storeId,
                             ReviewDto request,
                             MultipartFile receiptImage,
                             List<MultipartFile> images) {

        // 영수증인지 확인
        if (!upstageService.isReceipt(receiptImage)) {
            throw new IllegalArgumentException("업로드된 파일은 영수증이 아닙니다.");
        }

        // OCR 파싱
        ReceiptInfoDto receiptInfoDto = upstageService.extractReceiptInfo(receiptImage);

        // Store 조회
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 가게가 존재하지 않습니다."));

        // 가게명 매칭
        if (!isMatchingName(store.getFacltNm(), receiptInfoDto.storeName())) {
            throw new IllegalArgumentException("영수증의 가게명과 선택한 가게가 일치하지 않습니다.");
        }

        Review review = reviewRepository.save(
                Review.builder()
                        .keywords(request.keywords())
                        .store(store)
                        .build()
        );

        // 이미지 업로드
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

    private boolean isMatchingName(String dbName, String ocrName) {
        if (dbName == null || ocrName == null) {
            return false;
        }
        dbName = dbName.replace(" ", "");
        ocrName = ocrName.replace(" ", "");
        return ocrName.contains(dbName) || dbName.contains(ocrName);
    }

    @Transactional(readOnly = true)
    public ReviewListDto getReview(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("가맹점을 찾을 수 없습니다."));

        List<Review> reviews = reviewRepository.findByStore(store);

        List<ReviewInfoDto> reviewInfoDtos = reviews.stream()
                .map(review -> {
                    List<ReviewImage> images = reviewImageRepository.findByReview(review);
                    return ReviewInfoDto.from(
                            review.getId(),
                            images,
                            review.getKeywords()
                    );
                })
                .toList();

        return new ReviewListDto(storeId, reviewInfoDtos);
    }
}
