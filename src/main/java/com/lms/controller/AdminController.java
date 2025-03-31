package com.lms.controller;

import com.lms.dto.response.AdminDashboardResponse;
import com.lms.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardResponse> getDashboardStats() {
        return ResponseEntity.ok(adminService.getDashboardStats());
    }

    @PostMapping("/courses/{courseId}/approve")
    public ResponseEntity<String> approveCourse(@PathVariable String courseId) {
        adminService.approveCourse(courseId);
        return ResponseEntity.ok("Course approved successfully");
    }

    @PostMapping("/users/{userId}/ban")
    public ResponseEntity<String> banUser(@PathVariable String userId) {
        adminService.banUser(userId);
        return ResponseEntity.ok("User banned successfully");
    }
}