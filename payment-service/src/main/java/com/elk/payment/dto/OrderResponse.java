package com.elk.payment.dto;

import com.elk.payment.util.OrderStatus;

import java.math.BigDecimal;

public record OrderResponse(
        Long id,
        String orderNumber,
        String customerId,
        OrderStatus status,
        BigDecimal totalAmount
) {}