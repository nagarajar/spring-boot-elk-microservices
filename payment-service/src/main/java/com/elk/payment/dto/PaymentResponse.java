package com.elk.payment.dto;

import com.elk.payment.util.PaymentMethod;
import com.elk.payment.util.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(

        Long id,
        Long orderId,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        PaymentStatus status,
        LocalDateTime createdAt

) {}