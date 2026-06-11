package com.keycap.keycapdesign.service;

import com.keycap.keycapdesign.dto.payment.PaymentTransactionResponse;
import com.keycap.keycapdesign.entity.Order;
import com.keycap.keycapdesign.entity.PaymentTransaction;
import com.keycap.keycapdesign.enums.PaymentStatus;
import com.keycap.keycapdesign.enums.Role;
import com.keycap.keycapdesign.enums.TransactionDirection;
import com.keycap.keycapdesign.enums.TransactionType;
import com.keycap.keycapdesign.repository.PaymentTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
public class PaymentTransactionService {
    private final PaymentTransactionRepository transactionRepository;

    public PaymentTransactionService(PaymentTransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public void recordPayment(Order order, BigDecimal amount, String externalReference, String destination,
            LocalDateTime occurredAt, boolean authoritative) {
        PaymentTransaction existing = transactionRepository.findByOrderIdAndType(order.getId(), TransactionType.PAYMENT)
                .orElse(null);
        if (existing != null) {
            if (!authoritative) return;
            existing.setExternalReference(externalReference);
            existing.setDestination(destination);
            existing.setOccurredAt(occurredAt);
            existing.setAmount(amount);
            transactionRepository.save(existing);
            return;
        }
        save(order, amount, PaymentStatus.PAID, TransactionType.PAYMENT, TransactionDirection.OUTGOING,
                Role.CUSTOMER, destination, externalReference, occurredAt);
    }

    @Transactional
    public void recordRefund(Order order) {
        if (transactionRepository.existsByOrderIdAndType(order.getId(), TransactionType.REFUND)) return;
        String destination = order.getUser().getBankAccount();
        save(order, order.getTotalAmount(), PaymentStatus.REFUNDED, TransactionType.REFUND, TransactionDirection.INCOMING,
                Role.ADMIN, destination == null || destination.isBlank() ? "Tài khoản khách hàng" : destination,
                "REFUND-" + order.getOrderCode(), LocalDateTime.now());
    }

    public List<PaymentTransactionResponse> listByUser(Long userId) {
        return transactionRepository.findByUserIdOrderByOccurredAtDesc(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private void save(Order order, BigDecimal amount, PaymentStatus status, TransactionType type, TransactionDirection direction,
            Role actorRole, String destination, String externalReference, LocalDateTime occurredAt) {
        PaymentTransaction transaction = new PaymentTransaction();
        transaction.setOrder(order);
        transaction.setUser(order.getUser());
        transaction.setAmount(amount);
        transaction.setPaymentMethod(order.getPaymentMethod());
        transaction.setStatus(status);
        transaction.setType(type);
        transaction.setDirection(direction);
        transaction.setActorRole(actorRole);
        transaction.setDestination(destination);
        transaction.setExternalReference(externalReference);
        transaction.setOccurredAt(occurredAt);
        transactionRepository.save(transaction);
    }

    private PaymentTransactionResponse toResponse(PaymentTransaction transaction) {
        Order order = transaction.getOrder();
        return new PaymentTransactionResponse(transaction.getId(), order.getId(), order.getOrderCode(),
                transaction.getAmount(), transaction.getPaymentMethod(), transaction.getStatus(),
                transaction.getType(), transaction.getDirection(), transaction.getActorRole(),
                transaction.getDestination(), transaction.getExternalReference(), transaction.getOccurredAt());
    }
}
