package com.lms.service;

import com.lms.dto.request.CourseRequest;
import com.lms.dto.response.CourseResponse;
import com.lms.exception.AccessDeniedException;
import com.lms.exception.ResourceNotFoundException;
import java.util.List;

public interface CourseService {
 
    CourseResponse createCourse(CourseRequest request);

    List<CourseResponse> getAllCourses(String category, String search);

    CourseResponse getCourseById(String courseId) throws ResourceNotFoundException;

    
    CourseResponse updateCourse(String courseId, CourseRequest request) 
        throws ResourceNotFoundException, AccessDeniedException;

   
    void deleteCourse(String courseId) 
        throws ResourceNotFoundException, AccessDeniedException;

    void enrollInCourse(String courseId) 
        throws ResourceNotFoundException, IllegalStateException;
}