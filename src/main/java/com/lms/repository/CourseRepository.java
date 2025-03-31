package com.lms.repository;

import com.lms.model.Course;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends MongoRepository<Course, String> {
    @Query("{ 'instructorId': ?0 }")
    List<Course> findByInstructor(String instructorId);
    
    @Query("{ 'categoryId': ?0, 'isPublished': true }")
    List<Course> findByCategoryAndPublished(String categoryId);
    
    @Query("{ 'title': { $regex: ?0, $options: 'i' }, 'isPublished': true }")
    List<Course> searchPublishedCourses(String searchTerm);
    
    @Query("{ 'isPublished': true }")
    List<Course> findAllPublished();
    
    @Query("{ 'isPublished': true, 'isApproved': true }")
    List<Course> findApprovedCourses();
    
    @Query(value = "{ 'instructorId': ?0 }", count = true)
    long countByInstructor(String instructorId);
    
    @Query(value = "{ 'categoryId': ?0 }", count = true)
    long countByCategory(String categoryId);

    @Query(value = "{ 'isApproved': false }", count = true)
    long countByIsApprovedFalse();

    List<Course> findByCategoryId(String categoryId);
    boolean existsByCategoryId(String categoryId);
    
    @Query("{ 'categoryId': ?0, 'title': { $regex: ?1, $options: 'i' } }")
    List<Course> findByCategoryIdAndTitleContainingIgnoreCase(String categoryId, String search);
    
    // Add these missing methods
    @Query("{ 'title': { $regex: ?0, $options: 'i' } }")
    List<Course> findByTitleContainingIgnoreCase(String search);
    
    boolean existsById(String courseId);
}