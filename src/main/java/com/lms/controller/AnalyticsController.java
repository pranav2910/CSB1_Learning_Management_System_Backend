package com.lms.controller;

import com.lms.dto.response.AnalyticsResponse;
import com.lms.dto.response.CoursePerformanceResponse;
import com.lms.dto.response.RevenueAnalyticsResponse;
import com.lms.dto.response.UserActivityResponse;
import com.lms.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/analytics")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/dashboard")
    public ResponseEntity<AnalyticsResponse> getDashboardAnalytics() {
        return ResponseEntity.ok(analyticsService.getDashboardAnalytics());
    }

    @GetMapping("/revenue")
    public ResponseEntity<RevenueAnalyticsResponse> getRevenueAnalytics(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        return ResponseEntity.ok(analyticsService.getRevenueAnalytics(startDate, endDate));
    }

    @GetMapping("/course-performance")
    public ResponseEntity<List<CoursePerformanceResponse>> getCoursePerformance() {
        return ResponseEntity.ok(analyticsService.getCoursePerformance());
    }

    @GetMapping("/user-activity")
    public ResponseEntity<UserActivityResponse> getUserActivityAnalytics() {
        return ResponseEntity.ok(analyticsService.getUserActivityAnalytics());
    }
}