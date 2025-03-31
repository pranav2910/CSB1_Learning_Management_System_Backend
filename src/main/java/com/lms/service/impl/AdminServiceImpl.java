package com.lms.service.impl;

import com.lms.dto.response.AdminDashboardResponse;
import com.lms.repository.CourseRepository;
import com.lms.repository.EnrollmentRepository;
import com.lms.repository.PaymentRepository;
import com.lms.repository.UserRepository;
import com.lms.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public AdminDashboardResponse getDashboardStats() {
        return AdminDashboardResponse.builder()
                .totalUsers(userRepository.count())
                .totalCourses(courseRepository.count())
                .totalEnrollments(enrollmentRepository.count())
                .totalRevenue(paymentRepository.sumCompletedPayments())
                .pendingCourseApprovals(courseRepository.countByIsApprovedFalse())
                .activeUsersLast30Days(userRepository.countRecentlyActiveUsers(LocalDateTime.now().minusDays(30)))
                .build();
    }

    @Override
    public void approveCourse(String courseId) {
        courseRepository.findById(courseId).ifPresent(course -> {
            course.setIsApproved(true);
            courseRepository.save(course);
        });
    }

    @Override
    public void banUser(String userId) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setActive(false);
            userRepository.save(user);
        });
    }
}