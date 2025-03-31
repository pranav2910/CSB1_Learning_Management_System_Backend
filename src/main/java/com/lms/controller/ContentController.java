package com.lms.controller;

import com.lms.dto.request.ContentRequest;
import com.lms.dto.response.ContentResponse;
import com.lms.service.ContentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses/{courseId}/content")
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    public ResponseEntity<ContentResponse> addContent(
            @PathVariable String courseId,
            @Valid @RequestBody ContentRequest request) {
        return ResponseEntity.ok(contentService.addContent(courseId, request));
    }

    @GetMapping
    public ResponseEntity<List<ContentResponse>> getAllContent(@PathVariable String courseId) {
        return ResponseEntity.ok(contentService.getAllContent(courseId));
    }

    @GetMapping("/{contentId}")
    public ResponseEntity<ContentResponse> getContentById(
            @PathVariable String courseId,
            @PathVariable String contentId) {
        return ResponseEntity.ok(contentService.getContentById(courseId, contentId));
    }

    @PutMapping("/{contentId}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    public ResponseEntity<ContentResponse> updateContent(
            @PathVariable String courseId,
            @PathVariable String contentId,
            @Valid @RequestBody ContentRequest request) {
        return ResponseEntity.ok(contentService.updateContent(courseId, contentId, request));
    }

    @DeleteMapping("/{contentId}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    public ResponseEntity<String> deleteContent(
            @PathVariable String courseId,
            @PathVariable String contentId) {
        contentService.deleteContent(courseId, contentId);
        return ResponseEntity.ok("Content deleted successfully");
    }
}