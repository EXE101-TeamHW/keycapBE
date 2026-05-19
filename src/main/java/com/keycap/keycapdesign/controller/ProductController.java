package com.keycap.keycapdesign.controller;

import com.keycap.keycapdesign.common.ApiResponse;
import com.keycap.keycapdesign.dto.product.ProductResponse;
import com.keycap.keycapdesign.dto.review.ReviewRequest;
import com.keycap.keycapdesign.dto.review.ReviewResponse;
import com.keycap.keycapdesign.service.ProductService;
import com.keycap.keycapdesign.service.ReviewService;
import com.keycap.keycapdesign.enums.KeyProfile;
import com.keycap.keycapdesign.enums.LayoutType;
import com.keycap.keycapdesign.enums.ProductTheme;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;
    private final ReviewService reviewService;

    public ProductController(ProductService productService, ReviewService reviewService) {
        this.productService = productService;
        this.reviewService = reviewService;
    }

    @GetMapping
    public ApiResponse<List<ProductResponse>> list(@RequestParam(required = false) ProductTheme theme,
                                                   @RequestParam(required = false) LayoutType layoutType,
                                                   @RequestParam(required = false) KeyProfile keyProfile,
                                                   @RequestParam(required = false) BigDecimal minPrice,
                                                   @RequestParam(required = false) BigDecimal maxPrice) {
        return ApiResponse.success(productService.listProducts(theme, layoutType, keyProfile, minPrice, maxPrice));
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> get(@PathVariable Long id) {
        return ApiResponse.success(productService.getProduct(id));
    }

    @PostMapping("/{id}/reviews")
    public ApiResponse<ReviewResponse> createReview(@PathVariable Long id,
                                                    @RequestParam(required = false) Long orderId,
                                                    @Valid @RequestBody ReviewRequest request) {
        return ApiResponse.success(reviewService.createReview(id, orderId, request));
    }

    @GetMapping("/{id}/reviews")
    public ApiResponse<List<ReviewResponse>> listReviews(@PathVariable Long id) {
        return ApiResponse.success(reviewService.listByProduct(id));
    }
}
