package com.elk.order.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductResponse(
        Long productId,
        String productName,
        BigDecimal price
) { }
