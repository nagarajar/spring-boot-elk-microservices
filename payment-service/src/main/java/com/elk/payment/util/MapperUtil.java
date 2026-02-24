package com.elk.payment.util;

import com.elk.payment.dto.ApiResponse;
import com.elk.payment.dto.ErrorResponse;
import com.elk.payment.dto.PaymentResponse;
import com.elk.payment.entity.Payment;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

public class MapperUtil {

    public static <T> ApiResponse<T> buildAPiResponse(HttpStatus status, String message, T data, String path){
        return ApiResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .message(message)
                .path(path)
                .data(data)
                .build();
    }

    public static ErrorResponse buildErrorResponse(HttpStatus status, String errorCode, String message, String path, Map<String, String> fieldError) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(errorCode)
                .message(message)
                .path(path)
                .fieldErrors(fieldError)
                .build();
    }

    public static PaymentResponse buildPaymentResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getOrderId(),
                payment.getAmount(),
                payment.getPaymentMethod(),
                payment.getStatus(),
                payment.getCreatedAt()
        );
    }
}
