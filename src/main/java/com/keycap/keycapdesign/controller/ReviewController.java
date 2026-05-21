package com.keycap.keycapdesign.controller;

import com.keycap.keycapdesign.common.ApiResponse;
import com.keycap.keycapdesign.dto.review.ReviewRequest;
import com.keycap.keycapdesign.dto.review.ReviewResponse;
import com.keycap.keycapdesign.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products/{productId}/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * POST /api/products/{productId}/reviews?orderId=X (JWT - Customer đã mua)
     * Body: { userId, rating (1-5), comment }
     */
    @PostMapping
    public ApiResponse<ReviewResponse> create(
            @PathVariable Long productId,
            @RequestParam(required = false) Long orderId,
            @Valid @RequestBody ReviewRequest request) {
        return ApiResponse.success(reviewService.createReview(productId, orderId, request));
    }

    /**
     * GET /api/products/{productId}/reviews (Public)
     */
    @GetMapping
    public ApiResponse<List<ReviewResponse>> list(@PathVariable Long productId) {
        return ApiResponse.success(reviewService.listByProduct(productId));
    }
}
