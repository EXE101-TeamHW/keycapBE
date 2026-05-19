package com.keycap.keycapdesign.repository;

import com.keycap.keycapdesign.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
	java.util.List<OrderItem> findByOrderId(Long orderId);
}


