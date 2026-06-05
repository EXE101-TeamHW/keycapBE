package com.keycap.keycapdesign.dto.custom;

import com.keycap.keycapdesign.enums.LayoutType;
import com.keycap.keycapdesign.enums.ProductTheme;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public class CustomCheckoutRequest {
    private Long userId;

    @NotBlank
    private String designName;

    private LayoutType layout;
    private ProductTheme theme;
    private List<String> referenceImages;
    private String notes;

    @NotBlank
    private String shippingAddress;

    @NotNull
    private BigDecimal totalAmount;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDesignName() {
        return designName;
    }

    public void setDesignName(String designName) {
        this.designName = designName;
    }

    public LayoutType getLayout() {
        return layout;
    }

    public void setLayout(LayoutType layout) {
        this.layout = layout;
    }

    public ProductTheme getTheme() {
        return theme;
    }

    public void setTheme(ProductTheme theme) {
        this.theme = theme;
    }

    public List<String> getReferenceImages() {
        return referenceImages;
    }

    public void setReferenceImages(List<String> referenceImages) {
        this.referenceImages = referenceImages;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
