package com.lms.controller;

import com.lms.dto.request.ReviewRequest;
import com.lms.dto.response.ReviewResponse;
import com.lms.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses/{courseId}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(
            @PathVariable String courseId,
            @Valid @RequestBody ReviewRequest request) {
        return ResponseEntity.ok(reviewService.createReview(request));
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponse>> getAllReviews(@PathVariable String courseId) {
        return ResponseEntity.ok(reviewService.getAllReviews(courseId));
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> updateReview(
            @PathVariable String courseId,
            @PathVariable String reviewId,
            @Valid @RequestBody ReviewRequest request) {
        return ResponseEntity.ok(reviewService.updateReview(courseId, reviewId, request));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(
            @PathVariable String courseId,
            @PathVariable String reviewId) {
        reviewService.deleteReview(courseId, reviewId);
        return ResponseEntity.ok("Review deleted successfully");
    }
}