package com.keycap.keycapdesign.repository;

import com.keycap.keycapdesign.entity.OrderItem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
	@EntityGraph(attributePaths = {"product", "order"})
	java.util.List<OrderItem> findByOrderId(Long orderId);

	@EntityGraph(attributePaths = {"product", "order"})
	java.util.List<OrderItem> findByOrderIdIn(java.util.List<Long> orderIds);
}


