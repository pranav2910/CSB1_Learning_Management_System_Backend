package com.lms.service.impl;

import com.lms.dto.response.*;
import com.lms.model.Course;
import com.lms.model.Payment;
import com.lms.repository.*;
import com.lms.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final PaymentRepository paymentRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public AnalyticsResponse getDashboardAnalytics() {
        return AnalyticsResponse.builder()
                .totalUsers(userRepository.count())
                .totalCourses(courseRepository.count())
                .totalEnrollments(enrollmentRepository.count())
                .totalRevenue(paymentRepository.sumCompletedPayments())
                .pendingCourseApprovals(courseRepository.countByIsApprovedFalse())
                .activeUsersLast30Days(userRepository.countRecentlyActiveUsers(LocalDateTime.now().minusDays(30)))
                .build();
    }

    @Override
    public RevenueAnalyticsResponse getRevenueAnalytics(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate != null ? startDate.atStartOfDay() : LocalDate.now().minusMonths(1).atStartOfDay();
        LocalDateTime endDateTime = endDate != null ? endDate.atTime(23, 59, 59) : LocalDateTime.now();

        List<Payment> completedPayments = paymentRepository.findCompletedPaymentsBetweenDates(startDateTime, endDateTime);
    
        // Convert Date to LocalDate for grouping
        Map<LocalDate, BigDecimal> dailyRevenue = completedPayments.stream()
            .collect(Collectors.groupingBy(
                payment -> payment.getPaymentDate().toLocalDate(),
                Collectors.reducing(BigDecimal.ZERO, Payment::getAmount, BigDecimal::add)
            ));

        return RevenueAnalyticsResponse.builder()
                .totalRevenue(paymentRepository.getSumCompletedPaymentsBetweenDates(startDateTime, endDateTime))
                .monthlyRevenue(paymentRepository.getSumCompletedPaymentsBetweenDates(
                        LocalDate.now().withDayOfMonth(1).atStartOfDay(),
                        LocalDateTime.now()))
                .yearlyRevenue(paymentRepository.getSumCompletedPaymentsBetweenDates(
                        LocalDate.now().withDayOfYear(1).atStartOfDay(),
                        LocalDateTime.now()))
                .dailyRevenue(dailyRevenue)
                .build();
    }

    @Override
    public List<CoursePerformanceResponse> getCoursePerformance() {
        return courseRepository.findAll().stream()
                .map(course -> CoursePerformanceResponse.builder()
                        .courseId(course.getId())
                        .courseTitle(course.getTitle())
                        .enrollments(enrollmentRepository.countByCourseId(course.getId()))
                        .revenue(paymentRepository.getSumCompletedPaymentsByCourseId(course.getId()))
                        .averageRating(reviewRepository.findAverageRatingByCourseId(course.getId()))
                        .completionRate(enrollmentRepository.calculateCompletionRate(course.getId()))
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public UserActivityResponse getUserActivityAnalytics() {
        return UserActivityResponse.builder()
                .newUsersLast30Days(userRepository.countUsersRegisteredBetween(
                        LocalDateTime.now().minusDays(30),
                        LocalDateTime.now()))
                .activeUsersLast7Days(userRepository.countRecentlyActiveUsers(
                        LocalDateTime.now().minusDays(7)))
                .activityByCountry(userRepository.countUsersByCountry())
                .userGrowthByMonth(userRepository.countUserGrowthByMonth())
                .build();
    }

    @Override
    public Map<String, Object> getCourseEnrollmentStats() {
        return courseRepository.findAll().stream()
                .collect(Collectors.toMap(
                        Course::getTitle,
                        course -> enrollmentRepository.countByCourseId(course.getId())
                ));
    }
}
