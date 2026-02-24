package com.elk.payment.service.impl;

import com.elk.payment.client.OrderClient;
import com.elk.payment.dto.*;
import com.elk.payment.entity.Payment;
import com.elk.payment.exception.ResourceNotFoundException;
import com.elk.payment.repository.PaymentRepository;
import com.elk.payment.service.PaymentService;
import com.elk.payment.util.MapperUtil;
import com.elk.payment.util.PaymentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderClient orderClient;

    @Override
    public PaymentResponse processPayment(PaymentRequest request) {

        log.info("Processing payment for orderId={}", request.orderId());

        // Call Order Service
        ApiResponse<OrderResponse> orderResponse =
                orderClient.getOrderById(request.orderId());

        OrderResponse order = orderResponse.getData();

        if (order == null) {
            throw new ResourceNotFoundException("Order not found");
        }

        // Basic simulation: Always SUCCESS
        Payment payment = Payment.builder()
                .orderId(order.id())
                .amount(order.totalAmount())
                .paymentMethod(request.paymentMethod())
                .status(PaymentStatus.SUCCESS)
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        log.info("Payment successful for orderId={}, paymentId={}",
                savedPayment.getOrderId(), savedPayment.getId());

        return MapperUtil.buildPaymentResponse(savedPayment);
    }

    @Override
    public PaymentResponse getPaymentById(Long paymentId) {
        log.info("Fetching payment details for paymentId={}", paymentId);

        Payment payment = paymentRepository.findById(paymentId).orElseThrow(() ->
                new ResourceNotFoundException("Payment not found with id: " + paymentId));
        return MapperUtil.buildPaymentResponse(payment);
    }

    @Override
    public List<PaymentResponse> getAllPayments() {
        log.info("Fetching all payments");
        List<Payment> payments = paymentRepository.findAll();
        return payments.stream()
                .map(MapperUtil::buildPaymentResponse)
                .toList();
    }

    @Override
    public List<PaymentResponse> getPaymentsByOrderId(Long orderId) {
        log.info("Fetching payments for orderId={}", orderId);
        List<Payment> payments = paymentRepository.findByOrderId(orderId);
        return payments.stream()
                .map(MapperUtil::buildPaymentResponse)
                .toList();
    }

    @Override
    public List<PaymentResponse> getPaymentsByStatus(String status) {
        log.info("Fetching payments with status={}", status);
        PaymentStatus paymentStatus;
        try {
            paymentStatus = PaymentStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid payment status: " + status);
        }

        List<Payment> payments = paymentRepository.findByStatus(paymentStatus);
        return payments.stream()
                .map(MapperUtil::buildPaymentResponse)
                .toList();
    }
}