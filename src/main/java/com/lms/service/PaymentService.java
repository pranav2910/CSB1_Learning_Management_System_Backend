package com.lms.service;

import com.lms.dto.request.PaymentRequest;
import com.lms.dto.response.PaymentResponse;
import com.lms.exception.PaymentFailedException;
import com.lms.exception.ResourceNotFoundException;

import java.util.List;

public interface PaymentService {
    PaymentResponse createPaymentIntent(PaymentRequest request) 
        throws ResourceNotFoundException, PaymentFailedException;
    
    void confirmPayment(String paymentId, String courseId) 
        throws ResourceNotFoundException, PaymentFailedException;
    
    List<PaymentResponse> getPaymentHistory();
}