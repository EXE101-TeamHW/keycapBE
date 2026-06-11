package com.keycap.keycapdesign.dto.payment;

import com.keycap.keycapdesign.enums.PaymentMethod;
import com.keycap.keycapdesign.enums.PaymentStatus;
import com.keycap.keycapdesign.enums.Role;
import com.keycap.keycapdesign.enums.TransactionDirection;
import com.keycap.keycapdesign.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentTransactionResponse {
    private final Long id;
    private final Long orderId;
    private final String orderCode;
    private final BigDecimal amount;
    private final PaymentMethod paymentMethod;
    private final PaymentStatus status;
    private final TransactionType type;
    private final TransactionDirection direction;
    private final Role actorRole;
    private final String destination;
    private final String externalReference;
    private final LocalDateTime createdAt;

    public PaymentTransactionResponse(Long id, Long orderId, String orderCode, BigDecimal amount,
            PaymentMethod paymentMethod, PaymentStatus status, TransactionType type,
            TransactionDirection direction, Role actorRole, String destination,
            String externalReference, LocalDateTime createdAt) {
        this.id = id;
        this.orderId = orderId;
        this.orderCode = orderCode;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.type = type;
        this.direction = direction;
        this.actorRole = actorRole;
        this.destination = destination;
        this.externalReference = externalReference;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public Long getOrderId() { return orderId; }
    public String getOrderCode() { return orderCode; }
    public BigDecimal getAmount() { return amount; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public PaymentStatus getStatus() { return status; }
    public TransactionType getType() { return type; }
    public TransactionDirection getDirection() { return direction; }
    public Role getActorRole() { return actorRole; }
    public String getDestination() { return destination; }
    public String getExternalReference() { return externalReference; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
