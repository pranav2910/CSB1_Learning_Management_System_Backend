package com.lms.controller;

import com.lms.dto.request.DiscussionRequest;
import com.lms.dto.response.DiscussionResponse;
import com.lms.service.DiscussionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses/{courseId}/discussions")
@RequiredArgsConstructor
public class DiscussionController {

    private final DiscussionService discussionService;

    @PostMapping
    public ResponseEntity<DiscussionResponse> createDiscussion(
            @PathVariable String courseId,
            @Valid @RequestBody DiscussionRequest request) {
        return ResponseEntity.ok(discussionService.createDiscussion(courseId, request));
    }

    @GetMapping
    public ResponseEntity<List<DiscussionResponse>> getAllDiscussions(
            @PathVariable String courseId) {
        return ResponseEntity.ok(discussionService.getAllDiscussions(courseId));
    }

    @PostMapping("/{discussionId}/reply")
    public ResponseEntity<DiscussionResponse> addReply(
            @PathVariable String courseId,
            @PathVariable String discussionId,
            @Valid @RequestBody DiscussionRequest request) {
        return ResponseEntity.ok(
            discussionService.addReply(courseId, discussionId, request));
    }

    @DeleteMapping("/{discussionId}")
    public ResponseEntity<String> deleteDiscussion(
            @PathVariable String courseId,
            @PathVariable String discussionId) {
        discussionService.deleteDiscussion(courseId, discussionId);
        return ResponseEntity.ok("Discussion deleted successfully");
    }
}