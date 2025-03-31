package com.lms.repository;

import com.lms.model.User;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    
    @Query("{ 'role': ?0 }")
    List<User> findByRole(String role);
    
    @Query("{ 'enabled': ?0 }")
    List<User> findByStatus(boolean enabled);
    
    boolean existsByEmail(String email);
    
    @Query(value = "{ 'name': { $regex: ?0, $options: 'i' } }")
    List<User> searchByName(String name);
    
    @Query(value = "{ 'verified': true }", count = true)
    long countVerifiedUsers();

    // Combined active users query with both active status and last activity date
    @Query(value = "{ 'active': true, 'lastActiveAt': { $gte: ?0 } }", count = true)
    long countRecentlyActiveUsers(LocalDateTime date);  // Renamed to be more specific

    // For counting users created between dates
    @Query(value = "{ 'createdAt': { $gte: ?0, $lte: ?1 } }", count = true)
    long countUsersRegisteredBetween(LocalDateTime start, LocalDateTime end);


    @Aggregation(pipeline = {
        "{ $group: { _id: '$country', count: { $sum: 1 } } }"
    })
    Map<String, Long> countUsersByCountry();
    
    @Aggregation(pipeline = {
        "{ $project: { month: { $month: '$createdAt' }, year: { $year: '$createdAt' } } }",
        "{ $group: { _id: { year: '$year', month: '$month' }, count: { $sum: 1 } } }",
        "{ $project: { _id: 0, period: { $concat: [ { $toString: '$_id.year' }, '-', { $toString: '$_id.month' } ] }, count: 1 } }"
    })
    Map<String, Long> countUserGrowthByMonth();
}