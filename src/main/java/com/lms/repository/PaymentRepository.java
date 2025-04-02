package com.lms.repository;

import com.lms.model.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {
    
    @Query("{ 'status': 'COMPLETED' }")
    List<Payment> findCompletedPayments();
    
    @Query(value = "{ 'status': 'COMPLETED' }", fields = "{ 'amount' : 1 }")
    List<Payment> findCompletedPaymentAmounts();
    
    default BigDecimal sumCompletedPayments() {
        return findCompletedPaymentAmounts().stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Aggregation(pipeline = {
        "{ $match: { status: 'COMPLETED', paymentDate: { $gte: ?0, $lte: ?1 } } }",
        "{ $group: { _id: null, total: { $sum: '$amount' } } }"
    })
    Double sumCompletedPaymentsBetweenDates(LocalDateTime start, LocalDateTime end);
    
    default BigDecimal getSumCompletedPaymentsBetweenDates(LocalDateTime start, LocalDateTime end) {
        Double result = sumCompletedPaymentsBetweenDates(start, end);
        return result != null ? BigDecimal.valueOf(result) : BigDecimal.ZERO;
    }
    
    @Query("{ 'status': 'COMPLETED', 'paymentDate': { $gte: ?0, $lte: ?1 } }")
    List<Payment> findCompletedPaymentsBetweenDates(LocalDateTime start, LocalDateTime end);
    
    @Aggregation(pipeline = {
        "{ $match: { status: 'COMPLETED', courseId: ?0 } }",
        "{ $group: { _id: null, total: { $sum: '$amount' } } }"
    })
    Double sumCompletedPaymentsByCourseId(String courseId);
    
    default BigDecimal getSumCompletedPaymentsByCourseId(String courseId) {
        Double result = sumCompletedPaymentsByCourseId(courseId);
        return result != null ? BigDecimal.valueOf(result) : BigDecimal.ZERO;
    }
    
    @Query("{ 'studentId': ?0 }")
    List<Payment> findByStudentId(String studentId);
}
