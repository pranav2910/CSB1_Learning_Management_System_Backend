package com.lms.repository;

import com.lms.model.Content;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContentRepository extends MongoRepository<Content, String> {
    List<Content> findByCourseId(String courseId);
    Optional<Content> findByIdAndCourseId(String id, String courseId);
    boolean existsByIdAndCourseId(String id, String courseId);
    void deleteByIdAndCourseId(String id, String courseId);
    void deleteByCourseId(String courseId);  // NEW METHOD ADDED
}