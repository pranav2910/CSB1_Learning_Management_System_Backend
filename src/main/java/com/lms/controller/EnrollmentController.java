package com.lms.controller;

import com.lms.dto.response.EnrollmentResponse;
import com.lms.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @GetMapping
    public ResponseEntity<List<EnrollmentResponse>> getUserEnrollments() {
        return ResponseEntity.ok(enrollmentService.getUserEnrollments());
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<EnrollmentResponse> getEnrollmentByCourse(
            @PathVariable String courseId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentByCourse(courseId));
    }

    @PostMapping("/{courseId}/complete")
    public ResponseEntity<EnrollmentResponse> markContentComplete(
            @PathVariable String courseId,
            @RequestParam String contentId) {
        return ResponseEntity.ok(enrollmentService.markContentComplete(courseId, contentId));
    }

    @GetMapping("/{courseId}/progress")
    public ResponseEntity<Double> getCourseProgress(
            @PathVariable String courseId) {
        return ResponseEntity.ok(enrollmentService.getCourseProgress(courseId));
    }
}