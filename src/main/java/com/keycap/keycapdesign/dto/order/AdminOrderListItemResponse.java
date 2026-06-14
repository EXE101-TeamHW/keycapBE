package com.keycap.keycapdesign.dto.order;

import com.keycap.keycapdesign.enums.OrderStatus;
import com.keycap.keycapdesign.enums.OrderType;
import com.keycap.keycapdesign.enums.PaymentStatus;
import com.keycap.keycapdesign.enums.PaymentMethod;
import com.keycap.keycapdesign.enums.PaymentType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record AdminOrderListItemResponse(
        Long id,
        String orderCode,
        Long customerId,
        String customerName,
        String customerEmail,
        String customerPhone,
        String customerBankAccount,
        OrderStatus status,
        PaymentStatus paymentStatus,
        PaymentMethod paymentMethod,
        PaymentType paymentType,
        BigDecimal totalAmount,
        OrderType type,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDate deliveryDeadline,
        String shippingAddress,
        String trackingNumber,
        String proofImagesJson,
        Long assignedStaffId,
        String assignedStaffName,
        Long ticketId,
        String ticketCode) {
}
