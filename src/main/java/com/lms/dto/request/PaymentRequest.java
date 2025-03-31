package com.lms.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PaymentRequest {
    @NotBlank(message = "Course ID is required")
    private String courseId;

    @NotBlank(message = "Payment method is required")
    @Pattern(regexp = "^(CARD|PAYPAL)$", 
             message = "Payment method must be CARD or PAYPAL")
    private String paymentMethod;

    @NotBlank(message = "Card token is required for card payments")
    private String paymentToken; // Stripe token or PayPal order ID

    @DecimalMin(value = "0.01", message = "Amount must be positive")
    private BigDecimal amount;

    private String couponCode;
}