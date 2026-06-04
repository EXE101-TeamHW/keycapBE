package com.keycap.keycapdesign.controller;

import com.keycap.keycapdesign.common.ApiResponse;
import com.keycap.keycapdesign.dto.review.ReviewRequest;
import com.keycap.keycapdesign.dto.review.ReviewResponse;
import com.keycap.keycapdesign.security.CurrentUserService;
import com.keycap.keycapdesign.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
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
    private final CurrentUserService currentUserService;

    public ReviewController(ReviewService reviewService, CurrentUserService currentUserService) {
        this.reviewService = reviewService;
        this.currentUserService = currentUserService;
    }

    /**
     * POST /api/products/{productId}/reviews?orderId=X (JWT - Customer đã mua)
     * Body: { userId, rating (1-5), comment }
     */
    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ApiResponse<ReviewResponse> create(
            @PathVariable Long productId,
            @RequestParam(required = false) Long orderId,
            @Valid @RequestBody ReviewRequest request) {
        request.setUserId(currentUserService.getCurrentUserId());
        return ApiResponse.success(reviewService.createReview(productId, orderId, request));
    }

    /**
     * GET /api/products/{productId}/reviews (Public)
     */
    @GetMapping
    public ApiResponse<List<ReviewResponse>> list(@PathVariable Long productId) {
        return ApiResponse.success(reviewService.listByProduct(productId));
    }

    /**
     * PUT /api/products/{productId}/reviews/{reviewId} (JWT - Customer sở hữu review)
     * Body: { rating (1-5), comment }
     */
    @PutMapping("/{reviewId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ApiResponse<ReviewResponse> update(
            @PathVariable Long productId,
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewRequest request) {
        request.setUserId(currentUserService.getCurrentUserId());
        return ApiResponse.success(reviewService.updateReview(productId, reviewId, request));
    }

    /**
     * DELETE /api/products/{productId}/reviews/{reviewId} (JWT - Customer sở hữu review)
     */
    @DeleteMapping("/{reviewId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ApiResponse<String> delete(
            @PathVariable Long productId,
            @PathVariable Long reviewId) {
        reviewService.deleteReview(productId, reviewId, currentUserService.getCurrentUserId());
        return ApiResponse.success("Deleted");
    }
}
