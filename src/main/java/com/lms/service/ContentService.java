package com.lms.service;

import com.lms.dto.request.ContentRequest;
import com.lms.dto.response.ContentResponse;
import java.util.List;

public interface ContentService {
    ContentResponse addContent(String courseId, ContentRequest request);
    List<ContentResponse> getAllContent(String courseId);
    ContentResponse getContentById(String courseId, String contentId);
    ContentResponse updateContent(String courseId, String contentId, ContentRequest request);
    void deleteContent(String courseId, String contentId);
}