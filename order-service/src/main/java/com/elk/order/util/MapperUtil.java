package com.elk.order.util;

import com.elk.order.dto.ApiResponse;
import com.elk.order.dto.ErrorResponse;
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
                .build();
    }
}
