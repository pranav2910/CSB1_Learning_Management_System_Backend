package com.lms.service;

import com.lms.dto.response.AdminDashboardResponse;

public interface AdminService {
    /**
     * Get comprehensive statistics for admin dashboard
     */
    AdminDashboardResponse getDashboardStats();
    
    /**
     * Approve a pending course
     * @param courseId ID of the course to approve
     */
    void approveCourse(String courseId);
    
    /**
     * Ban a user account
     * @param userId ID of the user to ban
     */
    void banUser(String userId);
    
    // Additional admin operations can be added here
}