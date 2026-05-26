package com.keycap.keycapdesign.dto.review;

import java.time.LocalDateTime;

public class ReviewResponse {
    private Long id;
    private Long orderId;
    private Long productId;
    private Long userId;
    private String userName;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;

    public ReviewResponse(Long id, Long orderId, Long productId, Long userId, String userName, Integer rating, String comment,
                          LocalDateTime createdAt) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.userId = userId;
        this.userName = userName;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public Integer getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

