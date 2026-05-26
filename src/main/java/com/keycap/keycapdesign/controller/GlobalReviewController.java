package com.keycap.keycapdesign.controller;

import com.keycap.keycapdesign.common.ApiResponse;
import com.keycap.keycapdesign.dto.review.ReviewResponse;
import com.keycap.keycapdesign.service.ReviewService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class GlobalReviewController {

    private final ReviewService reviewService;

    public GlobalReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * GET /api/reviews (Public)
     */
    @GetMapping
    public ApiResponse<List<ReviewResponse>> listAll() {
        return ApiResponse.success(reviewService.listAll());
    }
}
