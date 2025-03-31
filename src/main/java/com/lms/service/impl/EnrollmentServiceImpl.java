package com.lms.service.impl;

import com.lms.dto.response.EnrollmentResponse;
import com.lms.exception.ResourceNotFoundException;
import com.lms.model.Enrollment;
import com.lms.model.Course;
import com.lms.repository.EnrollmentRepository;
import com.lms.repository.CourseRepository;
import com.lms.service.EnrollmentService;
import com.lms.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;

    @Override
    public List<EnrollmentResponse> getUserEnrollments() {
        String studentId = SecurityUtils.getCurrentUserId();
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);
        
        return enrollments.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public EnrollmentResponse getEnrollmentByCourse(String courseId) {
        String studentId = SecurityUtils.getCurrentUserId();
        Enrollment enrollment = enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment", "studentId and courseId", 
                    studentId + "-" + courseId));
        
        return convertToResponse(enrollment);
    }

    @Override
    @Transactional
    public EnrollmentResponse markContentComplete(String courseId, String contentId) {
        String studentId = SecurityUtils.getCurrentUserId();
        
        Enrollment enrollment = enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment", "studentId and courseId", 
                    studentId + "-" + courseId));
        
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));

        if (course.getContentIds() == null || !course.getContentIds().contains(contentId)) {
            throw new ResourceNotFoundException("Content", "id", contentId);
        }

        if (enrollment.getCompletedContentIds() == null) {
            enrollment.setCompletedContentIds(new ArrayList<>());
        }

        if (!enrollment.getCompletedContentIds().contains(contentId)) {
            enrollment.getCompletedContentIds().add(contentId);
            
            double newProgress = calculateProgress(enrollment, course);
            enrollment.setProgress(newProgress);
            
            if (newProgress >= 100.0) {
                enrollment.setCompletedAt(new Date());
            }
            
            enrollment = enrollmentRepository.save(enrollment);
        }
        
        return convertToResponse(enrollment);
    }

    @Override
    public Double getCourseProgress(String courseId) {
        String studentId = SecurityUtils.getCurrentUserId();
        Enrollment enrollment = enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment", "studentId and courseId", 
                    studentId + "-" + courseId));
        
        return enrollment.getProgress() != null ? enrollment.getProgress() : 0.0;
    }

    private double calculateProgress(Enrollment enrollment, Course course) {
        if (course.getContentIds() == null || course.getContentIds().isEmpty()) {
            return 0.0;
        }
        if (enrollment.getCompletedContentIds() == null) {
            return 0.0;
        }
        return ((double) enrollment.getCompletedContentIds().size() / course.getContentIds().size()) * 100;
    }

    private EnrollmentResponse convertToResponse(Enrollment enrollment) {
        Course course = courseRepository.findById(enrollment.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", enrollment.getCourseId()));

        return EnrollmentResponse.builder()
                .id(enrollment.getId())
                .courseId(enrollment.getCourseId())
                .courseTitle(course.getTitle())
                .enrolledAt(convertToLocalDateTime(enrollment.getEnrolledAt()))
                .progressPercentage(enrollment.getProgress() != null ? enrollment.getProgress() : 0.0)
                .completedContentIds(enrollment.getCompletedContentIds() != null ? 
                    enrollment.getCompletedContentIds() : List.of())
                .completedAt(convertToLocalDateTime(enrollment.getCompletedAt()))
                .build();
    }

    private LocalDateTime convertToLocalDateTime(Date date) {
        return date != null ? 
            LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()) : 
            null;
    }

    @Override
@Transactional
public void enrollStudent(String studentId, String courseId) 
    throws ResourceNotFoundException, IllegalStateException {
    if (enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
        throw new IllegalStateException("Already enrolled in this course");
    }
    
    Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));
    
    Enrollment enrollment = Enrollment.builder()
            .studentId(studentId)
            .courseId(courseId)
            .enrolledAt(LocalDateTime.now())
            .progress(0.0)
            .isActive(true)
            .build();
    
    enrollmentRepository.save(enrollment);
}

   
}