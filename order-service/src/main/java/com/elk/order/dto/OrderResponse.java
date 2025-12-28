package com.elk.order.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record OrderResponse(
        Long id,
        String orderNumber,
        String customerId,
        String status,
        BigDecimal totalAmount,
        List<OrderItemResponse> items,
        OrderAuditResponse audit
) {
}
