package com.elk.payment.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.elk.payment.dto.ApiResponse;
import com.elk.payment.dto.PaymentRequest;
import com.elk.payment.dto.PaymentResponse;
import com.elk.payment.service.PaymentService;
import com.elk.payment.util.MapperUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<ApiResponse<PaymentResponse>> processPayment(@RequestBody PaymentRequest request) {
        log.info("Received payment request for orderId={}", request.orderId());

        PaymentResponse response = paymentService.processPayment(request);

        log.info("Payment processed successfully for orderId={}, paymentId={}",
                response.orderId(), response.id());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(MapperUtil.buildAPiResponse(
                   HttpStatus.CREATED,
                   "Payment processed successfully",
                   response, currentPath()
                ));
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPaymentById(Long paymentId) {
        log.info("Received request to fetch payment details for paymentId={}", paymentId);

        PaymentResponse response = paymentService.getPaymentById(paymentId);

        log.info("Fetched payment details for paymentId={}", paymentId);

        return ResponseEntity.ok(
                MapperUtil.buildAPiResponse(
                        HttpStatus.OK,
                        "Payment details fetched successfully",
                        response, currentPath()
                )
        );
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getPaymentsByOrderId(Long orderId) {
        log.info("Received request to fetch payments for orderId={}", orderId);

        List<PaymentResponse> responses = paymentService.getPaymentsByOrderId(orderId);

        log.info("Fetched {} payments for orderId={}", responses.size(), orderId);

        return ResponseEntity.ok(
                MapperUtil.buildAPiResponse(
                        HttpStatus.OK,
                        "Payments fetched successfully",
                        responses, currentPath()
                )
        );
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getPaymentsByStatus(String status) {
        log.info("Received request to fetch payments with status={}", status);

        List<PaymentResponse> responses = paymentService.getPaymentsByStatus(status);

        log.info("Fetched {} payments with status={}", responses.size(), status);

        return ResponseEntity.ok(
                MapperUtil.buildAPiResponse(
                        HttpStatus.OK,
                        "Payments fetched successfully",
                        responses, currentPath()
                )
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getAllPayments() {
        log.info("Received request to fetch all payments");

        List<PaymentResponse> responses = paymentService.getAllPayments();

        log.info("Fetched {} payments", responses.size());

        return ResponseEntity.ok(
                MapperUtil.buildAPiResponse(
                        HttpStatus.OK,
                        "All payments fetched successfully",
                        responses, currentPath()
                )
        );
    }

    private String currentPath() {
        return ServletUriComponentsBuilder.fromCurrentRequest().toUriString();
    }

}
