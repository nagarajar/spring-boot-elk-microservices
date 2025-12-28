package com.elk.order.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record OrderAuditResponse(
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime updatedAt,
        String updatedBy
) { }
