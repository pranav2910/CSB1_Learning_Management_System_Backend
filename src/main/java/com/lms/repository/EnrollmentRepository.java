package com.lms.repository;

import com.lms.model.Enrollment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends MongoRepository<Enrollment, String> {
    @Query(value = "{ 'courseId': ?0 }", count = true)
    long countByCourseId(String courseId);
    
    @Aggregation(pipeline = {
        "{ $match: { courseId: ?0 } }",
        "{ $group: { _id: null, completed: { $sum: { $cond: [{ $gte: ['$progress', 100] }, 1, 0] } }, total: { $sum: 1 } } }",
        "{ $project: { completionRate: { $multiply: [ { $divide: ['$completed', '$total'] }, 100 ] } } }"
    })
    Integer calculateCompletionRate(String courseId);

    @Query("{ 'studentId': ?0, 'courseId': ?1 }")
    Optional<Enrollment> findByStudentIdAndCourseId(String studentId, String courseId);
    
    @Query(value = "{ 'studentId': ?0, 'courseId': ?1 }", exists = true)
    boolean existsByStudentIdAndCourseId(String studentId, String courseId);
    
    @Query(value = "{ 'courseId': ?0 }", delete = true)
    void deleteByCourseId(String courseId);
    
    // NEW METHOD ADDED
    @Query("{ 'studentId': ?0 }")
    List<Enrollment> findByStudentId(String studentId);
}