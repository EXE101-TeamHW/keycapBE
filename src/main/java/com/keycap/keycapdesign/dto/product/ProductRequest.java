package com.keycap.keycapdesign.dto.product;

import com.keycap.keycapdesign.enums.ProductStatus;
import com.keycap.keycapdesign.enums.KeyProfile;
import com.keycap.keycapdesign.enums.LayoutType;
import com.keycap.keycapdesign.enums.ProductTheme;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public class ProductRequest {
    @NotBlank
    private String name;
    private String description;
    @NotNull
    private ProductTheme theme;
    @NotNull
    private LayoutType layoutType;
    @NotNull
    private KeyProfile keyProfile;

    @NotNull
    private BigDecimal price;

    private Integer stockQuantity;
    private List<String> images;
    private ProductStatus status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProductTheme getTheme() {
        return theme;
    }

    public void setTheme(ProductTheme theme) {
        this.theme = theme;
    }

    public LayoutType getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(LayoutType layoutType) {
        this.layoutType = layoutType;
    }

    public KeyProfile getKeyProfile() {
        return keyProfile;
    }

    public void setKeyProfile(KeyProfile keyProfile) {
        this.keyProfile = keyProfile;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }
}
