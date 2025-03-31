package com.lms.service.impl;

import com.lms.dto.request.CourseRequest;
import com.lms.dto.response.CourseResponse;
import com.lms.exception.AccessDeniedException;
import com.lms.exception.ResourceNotFoundException;
import com.lms.model.*;
import com.lms.repository.*;
import com.lms.security.SecurityUtils;
import com.lms.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ContentRepository contentRepository;

    @Override
    public CourseResponse createCourse(CourseRequest request) {
        String instructorId = SecurityUtils.getCurrentUserId();
        
        Course course = Course.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .price(request.getPrice())
                .instructorId(instructorId)
                .categoryId(request.getCategoryId())
                .learningObjectives(request.getLearningObjectives())
                .thumbnailUrl(request.getThumbnailUrl())
                .previewVideoUrl(request.getPreviewVideoUrl())
                .isPublished(request.getIsPublished())
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        
        Course savedCourse = courseRepository.save(course);
        return toResponse(savedCourse);
    }

    @Override
    public List<CourseResponse> getAllCourses(String category, String search) {
        List<Course> courses;
        
        if (category != null && search != null) {
            courses = courseRepository.findByCategoryIdAndTitleContainingIgnoreCase(category, search);
        } else if (category != null) {
            courses = courseRepository.findByCategoryId(category);
        } else if (search != null) {
            courses = courseRepository.findByTitleContainingIgnoreCase(search);
        } else {
            courses = courseRepository.findAll();
        }
        
        return courses.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CourseResponse getCourseById(String courseId) throws ResourceNotFoundException {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));
        return toResponse(course);
    }

    @Override
    public CourseResponse updateCourse(String courseId, CourseRequest request) 
            throws ResourceNotFoundException, AccessDeniedException {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));
        
        if (!course.getInstructorId().equals(SecurityUtils.getCurrentUserId())) {
            throw new AccessDeniedException("Only course instructor can update this course");
        }
        
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setPrice(request.getPrice());
        course.setCategoryId(request.getCategoryId());
        course.setLearningObjectives(request.getLearningObjectives());
        course.setThumbnailUrl(request.getThumbnailUrl());
        course.setPreviewVideoUrl(request.getPreviewVideoUrl());
        course.setIsPublished(request.getIsPublished());
        course.setUpdatedAt(new Date());
        
        Course updatedCourse = courseRepository.save(course);
        return toResponse(updatedCourse);
    }

    @Override
    public void deleteCourse(String courseId) 
            throws ResourceNotFoundException, AccessDeniedException {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));
        
        if (!course.getInstructorId().equals(SecurityUtils.getCurrentUserId())) {
            throw new AccessDeniedException("Only course instructor can delete this course");
        }
        
        // Delete associated contents first
        contentRepository.deleteByCourseId(courseId);
        // Then delete the course
        courseRepository.delete(course);
    }

    @Override
    public void enrollInCourse(String courseId) 
            throws ResourceNotFoundException, IllegalStateException {
        String studentId = SecurityUtils.getCurrentUserId();
        
        if (enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
            throw new IllegalStateException("Already enrolled in this course");
        }
        
        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException("Course", "id", courseId);
        }
        
        Enrollment enrollment = Enrollment.builder()
                .studentId(studentId)
                .courseId(courseId)
                .enrolledAt(new Date())
                .progress(0.0)
                .isActive(true)
                .build();
        
        enrollmentRepository.save(enrollment);
    }

    private CourseResponse toResponse(Course course) {
        Double averageRating = reviewRepository.findAverageRatingByCourseId(course.getId());
        averageRating = averageRating != null ? averageRating : 0.0;
        
        return CourseResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .price(course.getPrice())
                .instructorName(getInstructorName(course.getInstructorId()))
                .thumbnailUrl(course.getThumbnailUrl())
                .averageRating(averageRating)
                .totalStudents(enrollmentRepository.countByCourseId(course.getId()))
                .categoryName(getCategoryName(course.getCategoryId()))
                .createdAt(convertToLocalDateTime(course.getCreatedAt()))
                .isPublished(course.getIsPublished())
                .learningObjectives(course.getLearningObjectives())
                .totalContentMinutes(calculateTotalContentMinutes(course.getId()))
                .build();
    }
    
    private String getInstructorName(String instructorId) {
        return userRepository.findById(instructorId)
                .map(User::getName)
                .orElse("Unknown Instructor");
    }
    
    private String getCategoryName(String categoryId) {
        return categoryRepository.findById(categoryId)
                .map(Category::getName)
                .orElse("Uncategorized");
    }
    
    private Long calculateTotalContentMinutes(String courseId) {
        List<Content> contents = contentRepository.findByCourseId(courseId);
        return contents.stream()
                .mapToLong(content -> content.getDuration() != null ? content.getDuration() : 0L)
                .sum();
    }
    
    private LocalDateTime convertToLocalDateTime(Date date) {
        return date != null ? LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()) : null;
    }
}