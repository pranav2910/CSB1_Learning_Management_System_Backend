package com.lms.service.impl;
import com.lms.dto.request.ReviewRequest;
import com.lms.dto.response.ReviewResponse;
import com.lms.exception.ResourceNotFoundException;
import com.lms.model.Review;
import com.lms.model.User;
import com.lms.repository.ReviewRepository;
import com.lms.repository.UserRepository;
import com.lms.service.ReviewService;
import com.lms.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ReviewResponse createReview(String userId, String courseId, ReviewRequest request) {
        if (reviewRepository.existsByUserIdAndCourseId(userId, courseId)) {
            throw new IllegalStateException("User has already reviewed this course");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Review review = Review.builder()
                .courseId(courseId)
                .userId(userId)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        Review savedReview = reviewRepository.save(review);
        return convertToResponse(savedReview, user);
    }

    @Override
    public ReviewResponse getReviewById(String reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "id", reviewId));
        
        User user = userRepository.findById(review.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", review.getUserId()));
        
        return convertToResponse(review, user);
    }

    @Override
    public List<ReviewResponse> getReviewsByCourseId(String courseId) {
        return reviewRepository.findByCourseId(courseId).stream()
                .map(review -> {
                    User user = userRepository.findById(review.getUserId())
                            .orElseThrow(() -> new ResourceNotFoundException("User", "id", review.getUserId()));
                    return convertToResponse(review, user);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewResponse> getReviewsByUserId(String userId) {
        return reviewRepository.findByUserId(userId).stream()
                .map(review -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
                    return convertToResponse(review, user);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ReviewResponse updateReview(String userId, String reviewId, ReviewRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "id", reviewId));
        
        if (!review.getUserId().equals(userId)) {
            throw new IllegalStateException("User can only update their own reviews");
        }

        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setUpdatedAt(new Date());

        Review updatedReview = reviewRepository.save(review);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        return convertToResponse(updatedReview, user);
    }

    @Override
    @Transactional
    public void deleteReview(String userId, String reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "id", reviewId));
        
        if (!review.getUserId().equals(userId) && !SecurityUtils.hasRole("ADMIN")) {
            throw new IllegalStateException("Only review author or admin can delete reviews");
        }

        reviewRepository.delete(review);
    }

    @Override
    public Double getAverageRatingByCourseId(String courseId) {
        return reviewRepository.findAverageRatingByCourseId(courseId);
    }

    @Override
    public boolean hasUserReviewedCourse(String userId, String courseId) {
        return reviewRepository.existsByUserIdAndCourseId(userId, courseId);
    }

    private ReviewResponse convertToResponse(Review review, User user) {
        return ReviewResponse.builder()
                .id(review.getId())
                .courseId(review.getCourseId())
                .userId(review.getUserId())
                .userName(user.getName())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }
}