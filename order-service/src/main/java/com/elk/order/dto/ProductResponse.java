package com.elk.order.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record ProductResponse(
         Long id,
         String productCode,
         String name,
         String description,
         BigDecimal price,
         Integer stockQuantity,
         String status,
         LocalDateTime createdAt,
         LocalDateTime updatedAt
) { }
