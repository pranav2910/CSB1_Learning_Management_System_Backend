package com.lms.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "payments")
public class Payment {
    @Id
    private String id;
    private String studentId;
    private String courseId;
    private BigDecimal amount;
    private String paymentMethod; // STRIPE, PAYPAL
    private String transactionId;
    private String status; // PENDING, COMPLETED, FAILED
    private String receiptUrl;
    private String couponCode;
    
    @Builder.Default
    private BigDecimal discountAmount = BigDecimal.ZERO;
    
    @Builder.Default
    private LocalDateTime paymentDate = LocalDateTime.now();


    // Required no-args constructor for frameworks
    public Payment() {
    }


    // All-args constructor for builder
    public Payment(String id, String studentId, String courseId, BigDecimal amount, 
              String paymentMethod, String transactionId, String status, 
              String receiptUrl, String couponCode, BigDecimal discountAmount, 
              LocalDateTime paymentDate) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.transactionId = transactionId;
        this.status = status;
        this.receiptUrl = receiptUrl;
        this.couponCode = couponCode;
        this.discountAmount = discountAmount != null ? discountAmount : BigDecimal.ZERO;
        this.paymentDate = paymentDate != null ? paymentDate : LocalDateTime.now();
    }
}