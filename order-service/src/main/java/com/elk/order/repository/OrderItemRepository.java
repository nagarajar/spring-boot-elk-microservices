package com.elk.order.repository;

import com.elk.order.dto.OrderItemResponse;
import com.elk.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrderId(Long orderId);

    @Query("""
        SELECT new com.elk.order.dto.OrderItemResponse(
            oi.id,
            oi.productId,
            oi.productName,
            oi.price,
            oi.quantity,
            oi.totalPrice
        )
        FROM OrderItem oi
        WHERE oi.order.id = :orderId
    """)
    List<OrderItemResponse> findItemsByOrderId(Long orderId);
}
