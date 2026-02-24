package com.elk.payment.client;

import com.elk.payment.dto.ApiResponse;
import com.elk.payment.dto.OrderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ORDER-SERVICE")
public interface OrderClient {

    @GetMapping("/api/v1/orders/{id}")
    ApiResponse<OrderResponse> getOrderById(@PathVariable Long id);

}