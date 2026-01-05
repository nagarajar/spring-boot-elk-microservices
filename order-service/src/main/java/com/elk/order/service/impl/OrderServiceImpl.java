package com.elk.order.service.impl;

import com.elk.order.client.ProductClient;
import com.elk.order.dto.*;
import com.elk.order.entity.Order;
import com.elk.order.entity.OrderItem;
import com.elk.order.entity.OrderStatus;
import com.elk.order.exception.ResourceNotFoundException;
import com.elk.order.repository.OrderItemRepository;
import com.elk.order.repository.OrderRepository;
import com.elk.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository itemRepository;
    private final ProductClient productClient;

    @Override
    public OrderResponse createOrder(OrderRequest request) {
        log.info("Creating order for customerId={}", request.customerId());
        // Build Order
        Order order = Order.builder()
                .orderNumber(generateOrderNumber())
                .customerId(request.customerId())
                .status(OrderStatus.CREATED)
                .totalAmount(BigDecimal.ZERO)
                .build();

        // Build Order Items
        List<OrderItem> orderItems = request.items().stream()
                .map(item -> buildOrderItem(item, order))
                .toList();

        // Set Items & total amount to Order
        order.setItems(orderItems);
        order.setTotalAmount(calculateTotalAmount(orderItems));

        // Save Order
        Order savedOrder = orderRepository.save(order);
        log.info("Order created successfully orderId={}, OrderNumber={}",
                savedOrder.getId(), savedOrder.getOrderNumber());

        // Convert to Order Response
        return mapToOrderResponse(savedOrder);
    }


    // ----------------- Helpers -----------------
    private String generateOrderNumber() {
        return "ORD-"+ UUID.randomUUID().toString().substring(0,8);
    }

    private OrderItem buildOrderItem(OrderItemRequest itemRequest, Order order) {
        // Calculate total price for that item
        ProductResponse productResponse = getProductFromProductService(itemRequest.productId()).getData();
        log.info("Product response from the feign call : {} ", productResponse);
        BigDecimal totalPrice = productResponse.price().multiply(BigDecimal.valueOf(itemRequest.quantity()));
        return OrderItem.builder()
                .order(order)
                .productId(itemRequest.productId())
                .productName(productResponse.name())
                .price(productResponse.price())
                .quantity(itemRequest.quantity())
                .totalPrice(totalPrice)
                .build();
    }

    private BigDecimal calculateTotalAmount(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private OrderResponse mapToOrderResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .customerId(order.getCustomerId())
                .status(order.getStatus().name())
                .totalAmount(order.getTotalAmount())
                .audit(OrderAuditResponse.builder()
                        .createdBy(order.getCreatedBy())
                        .createdAt(order.getCreatedAt())
                        .updatedBy(order.getUpdatedBy())
                        .updatedAt(order.getUpdatedAt())
                        .build())
                .items(mapToOrderItemResponse(order.getItems()))
                .build();
    }

    private List<OrderItemResponse> mapToOrderItemResponse(List<OrderItem> items) {
        return items.stream()
                .map(item ->
                        OrderItemResponse.builder()
                                .id(item.getId())
                                .productId(item.getProductId())
                                .productName(item.getProductName())
                                .price(item.getPrice())
                                .quantity(item.getQuantity())
                                .totalPrice(item.getTotalPrice())
                                .build()
                    )
                .toList();
    }

    private ApiResponse<ProductResponse> getProductFromProductService(Long productId){
        //TODO: Replace with Product Service REST/Feign call
        return productClient.getProductById (productId);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long orderId) {
       Order order = orderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id="+orderId));
        return mapToOrderResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderByOrderNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with orderNumber="+orderNumber));
        return mapToOrderResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> findByCustomerId(String customerId) {
        return orderRepository.findByCustomerIdWithItems(customerId)
                .stream()
                .map(this::mapToOrderResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> findByOrderStatus(String orderStatus) {
        return orderRepository.findByStatus(OrderStatus.valueOf(orderStatus.toUpperCase()))
                .stream()
                .map(this::mapToOrderResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderSummaryResponse> findAllOrderSummaries() {
        return orderRepository.findAllOrderSummaries();
    }
}
