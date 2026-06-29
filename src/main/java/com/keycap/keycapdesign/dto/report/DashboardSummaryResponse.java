package com.keycap.keycapdesign.dto.report;

import java.math.BigDecimal;
import java.util.Map;

public record DashboardSummaryResponse(
        long customerCount,
        long successfulOrders,
        long cancelledOrders,
        long activeOrders,
        long totalOrders,
        long shopOrders,
        long customOrders,
        long collectedDepositOrders,
        long heldDepositOrders,
        long pendingRefundOrders,
        long refundedDepositOrders,
        BigDecimal totalRevenue,
        BigDecimal totalCollectedDeposits,
        BigDecimal totalHeldDeposits,
        BigDecimal totalPendingRefunds,
        BigDecimal totalRefundedDeposits,
        long reviewCount,
        BigDecimal averageReviewRating,
        Map<String, Long> orderStatusCounts) {
}
