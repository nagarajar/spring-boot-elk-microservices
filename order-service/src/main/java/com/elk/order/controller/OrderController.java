package com.elk.order.controller;

import com.elk.order.dto.ApiResponse;
import com.elk.order.dto.OrderRequest;
import com.elk.order.dto.OrderResponse;
import com.elk.order.dto.OrderSummaryResponse;
import com.elk.order.service.OrderService;
import com.elk.order.util.MapperUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@Slf4j
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderservice;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(
            @Valid @RequestBody OrderRequest orderRequest
            ) {
        log.info("Creating order for customerId={}", orderRequest.customerId());

        OrderResponse orderResponse=orderservice.createOrder(orderRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(MapperUtil.buildAPiResponse(
                   HttpStatus.CREATED,
                   "Order created successfully",
                   orderResponse, currentPath()
                ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(@PathVariable Long id){
        log.info("Fetching order with id={}", id);
        OrderResponse orderResponse = orderservice.getOrderById(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                MapperUtil.buildAPiResponse(
                        HttpStatus.OK,
                        "Order fetched successfully",
                        orderResponse,
                        currentPath()
                )
        );
    }

    @GetMapping("/orderNumber/{orderNumber}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderByOrderNumber(@PathVariable String orderNumber){
        log.info("Fetching order with orderNumber={}", orderNumber);
        OrderResponse orderResponse = orderservice.getOrderByOrderNumber(orderNumber);
        return ResponseEntity.status(HttpStatus.OK).body(
                MapperUtil.buildAPiResponse(
                        HttpStatus.OK,
                        "Order fetched by order number successfully",
                        orderResponse,
                        currentPath()
                )
        );
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrderByCustomerId(@PathVariable String customerId){
        log.info("Fetching orders for customerId={}", customerId);
        List<OrderResponse> orderResponses = orderservice.findByCustomerId(customerId);
        return ResponseEntity.status(HttpStatus.OK).body(
                MapperUtil.buildAPiResponse(
                        HttpStatus.OK,
                        "Customer orders fetched successfully",
                        orderResponses,
                        currentPath()
                )
        );
    }


    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrderByStatus(@PathVariable String status){
        log.info("Fetching orders with status={}", status);
        List<OrderResponse> orderResponses = orderservice.findByOrderStatus(status);
        return ResponseEntity.status(HttpStatus.OK).body(
                MapperUtil.buildAPiResponse(
                        HttpStatus.OK,
                        "Orders fetched by status successfully",
                        orderResponses,
                        currentPath()
                )
        );
    }

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<List<OrderSummaryResponse>>> getOrderSummaries(@PathVariable Long id){
        log.info("Fetching order summaries");
        List<OrderSummaryResponse> orderResponse = orderservice.findAllOrderSummaries();
        return ResponseEntity.status(HttpStatus.OK).body(
                MapperUtil.buildAPiResponse(
                        HttpStatus.OK,
                        "Order summaries fetched successfully",
                        orderResponse,
                        currentPath()
                )
        );
    }

    private String currentPath() {
        return ServletUriComponentsBuilder.fromCurrentRequest().toUriString();
    }

}
