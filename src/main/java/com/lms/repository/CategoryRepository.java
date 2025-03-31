package com.lms.repository;

import com.lms.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
    @Query("{ 'name': { $regex: ?0, $options: 'i' } }")
    List<Category> searchByName(String name);
    
    @Query(value = "{}", sort = "{ 'courseCount': -1 }")
    List<Category> findAllOrderByPopularity();
    
    @Query(value = "{ 'courseCount': { $gt: 0 } }")
    List<Category> findNonEmptyCategories();
    
    @Query(value = "{ 'name': ?0 }", exists = true)
    boolean existsByName(String name);

    @SuppressWarnings("null")
    Optional<Category> findById(String id);
    
    @Query(value = "{ 'name': ?0 }")
    Optional<Category> findByName(String name);
    
    @Query(value = "{ 'courseCount': { $gt: 0 } }", count = true)
    long countNonEmptyCategories();
}