package com.lms.repository;
import com.lms.model.Discussion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;
import java.util.Optional;

public interface DiscussionRepository extends MongoRepository<Discussion, String> {
    // Change this method to return Optional
    Optional<Discussion> findByIdAndCourseId(String id, String courseId);
    
    // Keep all other methods the same
    @Query("{ 'courseId': ?0, 'parentId': null }")
    List<Discussion> findByCourseIdAndParentIdIsNullOrderByCreatedAtDesc(String courseId);
    
    List<Discussion> findByParentIdOrderByCreatedAtAsc(String parentId);
    void deleteByParentId(String parentId);
    
    @Query("{ 'courseId': ?0, 'authorId': ?1 }")
    List<Discussion> findByCourseIdAndAuthorId(String courseId, String authorId);
}