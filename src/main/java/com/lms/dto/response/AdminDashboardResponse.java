package com.lms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO containing statistics for admin dashboard
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardResponse {
    private Long totalUsers;
    private Long totalCourses;
    private Long totalEnrollments;
    private BigDecimal totalRevenue;
    private Long pendingCourseApprovals;
    private Long activeUsersLast30Days;
    
    // Revenue breakdown (optional)
    private BigDecimal monthlyRevenue;
    private BigDecimal yearlyRevenue;
    
    // Popular courses (optional)
    private String topCourseId;
    private String topCourseTitle;
    private Long topCourseEnrollments;
}