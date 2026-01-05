package com.elk.order.repository;

import com.elk.order.dto.OrderResponse;
import com.elk.order.dto.OrderSummaryResponse;
import com.elk.order.entity.Order;
import com.elk.order.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Find by business key
    Optional<Order> findByOrderNumber(String orderNumber);

    // Find order with items (avoid N+1)
    @Query("""
            SELECT o from Order o
            JOIN FETCH o.items
            WHERE o.id = :orderId
    """)
    Optional<Order> findByIdWithItems(Long orderId);

    // Fetch by customer (with items)
    @Query("""
        SELECT DISTINCT o FROM Order o
        JOIN FETCH o.items
        WHERE o.customerId = :customerId
    """)
    List<Order> findByCustomerIdWithItems(String customerId);

    // Filter by status
    List<Order> findByStatus(OrderStatus status);

    @Query("""
        SELECT new com.elk.order.dto.OrderSummaryResponse(
            o.id,
            o.orderNumber,
            o.customerId,
            o.status,
            o.totalAmount
        )
        FROM Order o
        ORDER BY o.createdAt DESC
    """)
    List<OrderSummaryResponse> findAllOrderSummaries();


    @Query("""
    SELECT new com.elk.order.dto.OrderSummaryResponse(
        o.id,
        o.orderNumber,
        o.customerId,
        o.status,
        o.totalAmount
    )
    FROM Order o
    WHERE o.id = :orderId
    """)
    Optional<OrderSummaryResponse> findOrderSummaryById(@Param("orderId") Long orderId);

}
