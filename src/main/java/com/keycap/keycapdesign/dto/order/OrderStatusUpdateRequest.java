package com.keycap.keycapdesign.dto.order;

import com.keycap.keycapdesign.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;

public class OrderStatusUpdateRequest {
    @NotNull
    private OrderStatus status;

    private String trackingNumber;

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    private java.util.List<String> proofImages;

    public java.util.List<String> getProofImages() {
        return proofImages;
    }

    public void setProofImages(java.util.List<String> proofImages) {
        this.proofImages = proofImages;
    }
}

