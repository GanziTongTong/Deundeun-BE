package com.example.deundeun.review.api;

import com.example.deundeun.review.api.dto.request.ReviewDto;
import com.example.deundeun.review.application.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
@Tag(name = "리뷰 API", description = "리뷰 생성 API")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(
            summary = "리뷰 생성",
            description = "특정 가맹점에 대한 리뷰(키워드)와 이미지를 등록합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "등록 성공"),
                    @ApiResponse(responseCode = "400", description = "필수 파라미터 누락 또는 유효하지 않은 요청")
            }
    )
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Void> createReview(
            @Parameter(description = "리뷰를 등록할 가맹점 ID", required = true, example = "1")
            @RequestParam Long storeId,
            @RequestPart("request") ReviewDto request,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) {
        reviewService.createReview(storeId, request, images);
        return ResponseEntity.ok().build();
    }
}
