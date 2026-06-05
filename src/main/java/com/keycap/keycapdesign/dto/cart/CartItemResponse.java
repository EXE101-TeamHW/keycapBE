package com.keycap.keycapdesign.dto.cart;

import java.math.BigDecimal;

public class CartItemResponse {
    private Long id;
    private Long productId;
    private String productName;
    private String productImage;
    private Integer quantity;
    private BigDecimal unitPrice;
    private Integer stockQuantity;

    public CartItemResponse(Long id, Long productId, String productName, String productImage,
                            Integer quantity, BigDecimal unitPrice, Integer stockQuantity) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.productImage = productImage;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.stockQuantity = stockQuantity;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }
}

