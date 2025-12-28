package com.elk.order.service;

import com.elk.order.dto.OrderRequest;
import com.elk.order.dto.OrderResponse;
import com.elk.order.dto.OrderSummaryResponse;

import java.util.List;

public interface OrderService {

    OrderResponse createOrder(OrderRequest request);

    OrderResponse getOrderById(Long orderId);

    OrderResponse getOrderByOrderNumber(String orderNumber);

    List<OrderResponse> findByCustomerId(String customerId);

    List<OrderResponse> findByOrderStatus(String orderStatus);

    List<OrderSummaryResponse> findAllOrderSummaries();
}
