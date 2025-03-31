package com.lms.service;

import com.lms.dto.response.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface AnalyticsService {
    AnalyticsResponse getDashboardAnalytics();
    RevenueAnalyticsResponse getRevenueAnalytics(LocalDate startDate, LocalDate endDate);
    List<CoursePerformanceResponse> getCoursePerformance();
    UserActivityResponse getUserActivityAnalytics();
    Map<String, Object> getCourseEnrollmentStats();
}