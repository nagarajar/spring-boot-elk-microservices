package com.elk.order.dto;

import com.elk.order.entity.OrderStatus;

import java.math.BigDecimal;

public record OrderSummaryResponse(
        Long id,
        String orderNumber,
        String customerId,
        OrderStatus status,
        BigDecimal totalAmount
) { }
