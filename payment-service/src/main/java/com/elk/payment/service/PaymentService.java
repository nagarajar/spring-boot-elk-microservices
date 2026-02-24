package com.elk.payment.service;

import java.util.List;

import com.elk.payment.dto.PaymentRequest;
import com.elk.payment.dto.PaymentResponse;

public interface PaymentService {

    PaymentResponse processPayment(PaymentRequest request);

    PaymentResponse getPaymentById(Long paymentId);

    List<PaymentResponse> getAllPayments();

    List<PaymentResponse> getPaymentsByOrderId(Long orderId);

    List<PaymentResponse> getPaymentsByStatus(String status);

}