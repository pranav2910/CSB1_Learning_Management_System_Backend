package com.lms.service;

import com.lms.dto.request.ReviewRequest;
import com.lms.dto.response.ReviewResponse;
import java.util.List;

public interface ReviewService {
    ReviewResponse createReview(String userId, String courseId, ReviewRequest request);
    ReviewResponse getReviewById(String reviewId);
    List<ReviewResponse> getReviewsByCourseId(String courseId);
    List<ReviewResponse> getReviewsByUserId(String userId);
    ReviewResponse updateReview(String userId, String reviewId, ReviewRequest request);
    void deleteReview(String userId, String reviewId);
    Double getAverageRatingByCourseId(String courseId);
    boolean hasUserReviewedCourse(String userId, String courseId);
}