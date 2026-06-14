package com.keycap.keycapdesign.dto.ticket;

import com.keycap.keycapdesign.enums.TicketStatus;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.math.BigDecimal;

public record StaffTicketListItemResponse(
        Long id,
        String ticketCode,
        Long customerId,
        String customerName,
        String customerEmail,
        String customerPhone,
        String customerBankAccount,
        String requestDesignName,
        String referenceImagesJson,
        String notes,
        String layout,
        String theme,
        LocalDate deadline,
        Integer revisionCount,
        BigDecimal quotedPrice,
        TicketStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long assignedStaffId,
        String assignedStaffName,
        String adminName,
        Long orderId,
        String orderCode,
        String orderStatus,
        String orderPaymentStatus) {
}
