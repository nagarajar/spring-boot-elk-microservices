package com.elk.product.mapper;

import com.elk.product.dto.ApiResponse;
import com.elk.product.dto.ErrorResponse;
import com.elk.product.dto.ProductRequest;
import com.elk.product.dto.ProductResponse;
import com.elk.product.entity.Product;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

public class MapperUtil {

    public static Product toProductEntity(ProductRequest request) {
        return Product.builder()
                .productCode(request.getProductCode())
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stockQuantity(request.getStockQuantity())
                .status(request.getStatus())
                .build();
    }

    public static ProductResponse toProductResponse(Product entity) {
        return ProductResponse.builder()
                .id(entity.getId())
                .productCode(entity.getProductCode())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .stockQuantity(entity.getStockQuantity())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static ErrorResponse buildErrorResponse(HttpStatus status, String errorCode, String message,
                                      String path, Map<String, String> fieldErrors) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(errorCode)
                .message(message)
                .fieldErrors(fieldErrors)
                .path(path)
                .build();
    }

    public static  <T> ApiResponse<T> buildApiResponse(HttpStatus status, String message,
            T data, String path) {
        return ApiResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .message(message)
                .data(data)     // can be null (DELETE, etc.)
                .path(path)
                .build();
    }
}

