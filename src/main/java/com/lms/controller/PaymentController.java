package com.lms.controller;

import com.lms.dto.request.PaymentRequest;
import com.lms.dto.response.PaymentResponse;
import com.lms.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/checkout")
    public ResponseEntity<PaymentResponse> createPaymentIntent(
            @Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.ok(paymentService.createPaymentIntent(request));
    }

    @GetMapping("/success")
    public ResponseEntity<String> paymentSuccess(
            @RequestParam String paymentId,
            @RequestParam String courseId) {
        paymentService.confirmPayment(paymentId, courseId);
        return ResponseEntity.ok("Payment confirmed and enrollment completed successfully");
    }

    @GetMapping("/history")
    public ResponseEntity<List<PaymentResponse>> getPaymentHistory() {
        return ResponseEntity.ok(paymentService.getPaymentHistory());
    }
}