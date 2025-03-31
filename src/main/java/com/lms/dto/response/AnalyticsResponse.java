package com.lms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsResponse {
    private Long totalUsers;
    private Long totalCourses;
    private Long totalEnrollments;
    private BigDecimal totalRevenue;
    private Long pendingCourseApprovals;
    private Long activeUsersLast30Days;
}