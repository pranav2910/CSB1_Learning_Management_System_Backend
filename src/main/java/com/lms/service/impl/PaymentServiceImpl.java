package com.lms.service.impl;

import com.lms.dto.request.PaymentRequest;
import com.lms.dto.response.PaymentResponse;
import com.lms.exception.PaymentFailedException;
import com.lms.exception.ResourceNotFoundException;
import com.lms.model.Course;
import com.lms.model.Payment;
import com.lms.model.User;
import com.lms.repository.CourseRepository;
import com.lms.repository.PaymentRepository;
import com.lms.repository.UserRepository;
import com.lms.security.SecurityUtils;
import com.lms.service.EnrollmentService;
import com.lms.service.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final EnrollmentService enrollmentService;
    
    @Value("${stripe.secret-key}")
    private String stripeSecretKey;
    
    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    @Override
    @Transactional
    public PaymentResponse createPaymentIntent(PaymentRequest request) 
            throws ResourceNotFoundException, PaymentFailedException {
        String userId = SecurityUtils.getCurrentUserId();
        @SuppressWarnings("unused")
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", request.getCourseId()));
        
        BigDecimal amount = calculateFinalAmount(course.getPrice(), request.getCouponCode());
        
        try {
            Map<String, Object> chargeParams = new HashMap<>();
            chargeParams.put("amount", amount.multiply(BigDecimal.valueOf(100)).intValue());
            chargeParams.put("currency", "usd");
            chargeParams.put("source", request.getPaymentToken());
            chargeParams.put("description", "Payment for course: " + course.getTitle());
            
            Charge charge = Charge.create(chargeParams);
            
            Payment payment = Payment.builder()
                    .studentId(userId)
                    .courseId(course.getId())
                    .amount(amount) // Stored as String internally
                    .paymentMethod(request.getPaymentMethod())
                    .transactionId(charge.getId())
                    .status(charge.getPaid() ? "COMPLETED" : "FAILED")
                    .receiptUrl(charge.getReceiptUrl())
                    .couponCode(request.getCouponCode())
                    .paymentDate(LocalDateTime.now())
                    .build();
            
            Payment savedPayment = paymentRepository.save(payment);
            
            if (!charge.getPaid()) {
                throw new PaymentFailedException("Payment failed: " + charge.getFailureMessage());
            }
            
            return toResponse(savedPayment, course.getTitle());
        } catch (StripeException e) {
            throw new PaymentFailedException("Stripe error: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void confirmPayment(String paymentId, String courseId) 
            throws ResourceNotFoundException, PaymentFailedException {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", paymentId));
        
        if (!payment.getCourseId().equals(courseId)) {
            throw new IllegalStateException("Payment does not match course");
        }
        
        if (!payment.getStatus().equals("COMPLETED")) {
            throw new PaymentFailedException("Payment not completed");
        }
        
        enrollmentService.enrollStudent(payment.getStudentId(), courseId);
    }

    @Override
    public List<PaymentResponse> getPaymentHistory() {
        String userId = SecurityUtils.getCurrentUserId();
        List<Payment> payments = paymentRepository.findByStudentId(userId);
        
        return payments.stream()
                .map(payment -> {
                    String courseTitle = courseRepository.findById(payment.getCourseId())
                            .map(Course::getTitle)
                            .orElse("Unknown Course");
                    return toResponse(payment, courseTitle);
                })
                .collect(Collectors.toList());
    }

    private BigDecimal calculateFinalAmount(BigDecimal originalAmount, String couponCode) {
        // Implement coupon logic here
        return originalAmount;
    }

    private PaymentResponse toResponse(Payment payment, String courseTitle) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .courseId(payment.getCourseId())
                .courseTitle(courseTitle)
                .amount(payment.getAmount()) // Automatically converts to BigDecimal
                .paymentMethod(payment.getPaymentMethod())
                .transactionId(payment.getTransactionId())
                .paymentDate(payment.getPaymentDate())
                .status(payment.getStatus())
                .receiptUrl(payment.getReceiptUrl())
                .build();
    }
}