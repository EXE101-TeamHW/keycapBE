package com.keycap.keycapdesign.repository.projection;

import java.math.BigDecimal;

public interface DashboardAggregateProjection {
    Number getSuccessfulOrders();

    Number getCancelledOrders();

    Number getActiveOrders();

    Number getTotalOrders();

    Number getShopOrders();

    Number getCustomOrders();

    Number getCollectedDepositOrders();

    Number getHeldDepositOrders();

    Number getPendingRefundOrders();

    Number getRefundedDepositOrders();

    BigDecimal getTotalRevenue();

    BigDecimal getTotalCollectedDeposits();

    BigDecimal getTotalHeldDeposits();

    BigDecimal getTotalPendingRefunds();

    BigDecimal getTotalRefundedDeposits();
}
