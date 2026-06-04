package com.keycap.keycapdesign.dto.ai;

import java.math.BigDecimal;

public class AiRecommendation {
    private Long productId;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private String imageUrl;
    private String theme;
    private String layoutType;
    private String keyProfile;
    private BigDecimal averageRating;
    private String reason;

    public AiRecommendation(Long productId, String name, String description, BigDecimal price, Integer stockQuantity,
            String imageUrl, String theme, String layoutType, String keyProfile, BigDecimal averageRating,
            String reason) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.imageUrl = imageUrl;
        this.theme = theme;
        this.layoutType = layoutType;
        this.keyProfile = keyProfile;
        this.averageRating = averageRating;
        this.reason = reason;
    }

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTheme() {
        return theme;
    }

    public String getLayoutType() {
        return layoutType;
    }

    public String getKeyProfile() {
        return keyProfile;
    }

    public BigDecimal getAverageRating() {
        return averageRating;
    }

    public String getReason() {
        return reason;
    }
}
