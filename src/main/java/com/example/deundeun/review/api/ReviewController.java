package com.example.deundeun.review.api;

import com.example.deundeun.review.api.dto.request.ReviewDto;
import com.example.deundeun.review.api.dto.response.ReviewListDto;
import com.example.deundeun.review.application.ReviewService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Void> createReview(
            @RequestParam Long storeId,
            @RequestPart("request") ReviewDto request,
            @RequestPart("receipt") MultipartFile receiptImage,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) {
        reviewService.createReview(storeId, request, receiptImage, images);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<ReviewListDto> getReview(@RequestParam Long storeId) {
        return ResponseEntity.ok(reviewService.getReview(storeId));
    }

}
