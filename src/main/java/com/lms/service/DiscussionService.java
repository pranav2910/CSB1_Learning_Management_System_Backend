package com.lms.service;

import com.lms.dto.request.DiscussionRequest;
import com.lms.dto.response.DiscussionResponse;
import java.util.List;

public interface DiscussionService {
    DiscussionResponse createDiscussion(String courseId, DiscussionRequest request);
    List<DiscussionResponse> getAllDiscussions(String courseId);
    DiscussionResponse addReply(String courseId, String discussionId, DiscussionRequest request);
    void deleteDiscussion(String courseId, String discussionId);
}