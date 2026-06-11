package com.keycap.keycapdesign.repository;

import com.keycap.keycapdesign.entity.PaymentTransaction;
import com.keycap.keycapdesign.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {
    List<PaymentTransaction> findByUserIdOrderByOccurredAtDesc(Long userId);
    boolean existsByOrderIdAndType(Long orderId, TransactionType type);
    Optional<PaymentTransaction> findByOrderIdAndType(Long orderId, TransactionType type);
}
