package com.lms.repository;

import com.lms.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Aggregation;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends MongoRepository<Review, String> {
    List<Review> findByCourseId(String courseId);
    List<Review> findByUserId(String userId);
    Optional<Review> findByIdAndCourseId(String id, String courseId);
    
    @Aggregation(pipeline = {
        "{ $match: { courseId: ?0 } }",
        "{ $group: { _id: null, averageRating: { $avg: '$rating' } } }"
    })
    Double findAverageRatingByCourseId(String courseId);
    
    @Query(value = "{ 'courseId': ?0 }", count = true)
    long countByCourseId(String courseId);
    
    boolean existsByUserIdAndCourseId(String userId, String courseId);
    
    // NEW METHOD ADDED FOR BETTER RATING CALCULATION
    @Query(value = "{ 'courseId': ?0, 'rating': { $exists: true } }", count = true)
    long countRatingsByCourseId(String courseId);
}