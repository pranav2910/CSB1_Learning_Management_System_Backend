package com.lms.service;

import com.lms.dto.response.EnrollmentResponse;
import com.lms.exception.ResourceNotFoundException;

import java.util.List;

public interface EnrollmentService {
    List<EnrollmentResponse> getUserEnrollments();
    EnrollmentResponse getEnrollmentByCourse(String courseId) throws ResourceNotFoundException;
    EnrollmentResponse markContentComplete(String courseId, String contentId) throws ResourceNotFoundException;
    Double getCourseProgress(String courseId) throws ResourceNotFoundException;
    void enrollInCourse(String courseId) throws ResourceNotFoundException, IllegalStateException;
}