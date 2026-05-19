package com.keycap.keycapdesign.dto.product;

import com.keycap.keycapdesign.enums.ProductStatus;
import com.keycap.keycapdesign.enums.KeyProfile;
import com.keycap.keycapdesign.enums.LayoutType;
import com.keycap.keycapdesign.enums.ProductTheme;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private List<String> images;
    private ProductStatus status;
    private LocalDateTime createdAt;
    private ProductTheme theme;
    private LayoutType layoutType;
    private KeyProfile keyProfile;

    public ProductResponse(Long id, String name, String description, BigDecimal price,
                           Integer stockQuantity, List<String> images, ProductStatus status, LocalDateTime createdAt,
                           ProductTheme theme, LayoutType layoutType, KeyProfile keyProfile) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.images = images;
        this.status = status;
        this.createdAt = createdAt;
        this.theme = theme;
        this.layoutType = layoutType;
        this.keyProfile = keyProfile;
    }

    public Long getId() {
        return id;
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

    public List<String> getImages() {
        return images;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public ProductTheme getTheme() {
        return theme;
    }

    public LayoutType getLayoutType() {
        return layoutType;
    }

    public KeyProfile getKeyProfile() {
        return keyProfile;
    }
}
