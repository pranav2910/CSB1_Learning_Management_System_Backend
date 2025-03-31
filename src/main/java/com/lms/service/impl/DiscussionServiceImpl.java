package com.lms.service.impl;
import com.lms.dto.request.DiscussionRequest;
import com.lms.dto.response.DiscussionResponse;
import com.lms.exception.ResourceNotFoundException;
import com.lms.exception.AccessDeniedException;
import com.lms.model.Discussion;
import com.lms.model.User;
import com.lms.repository.DiscussionRepository;
import com.lms.repository.CourseRepository;
import com.lms.repository.UserRepository;
import com.lms.service.DiscussionService;
import com.lms.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscussionServiceImpl implements DiscussionService {
    private final DiscussionRepository discussionRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Override
    public DiscussionResponse createDiscussion(String courseId, DiscussionRequest request) {
        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException("Course", "id", courseId);
        }

        String userId = SecurityUtils.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Discussion discussion = Discussion.builder()
                .courseId(courseId)
                .authorId(userId)
                .authorName(user.getName())
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .parentId(request.getParentId())
                .build();

        Discussion savedDiscussion = discussionRepository.save(discussion);
        return mapToResponseWithReplies(savedDiscussion);
    }

    @Override
    public List<DiscussionResponse> getAllDiscussions(String courseId) {
        List<Discussion> discussions = discussionRepository.findByCourseIdAndParentIdIsNullOrderByCreatedAtDesc(courseId);
        return discussions.stream()
                .map(this::mapToResponseWithReplies)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unused")
    @Override
    public DiscussionResponse addReply(String courseId, String discussionId, DiscussionRequest request) {
        Discussion parent = discussionRepository.findByIdAndCourseId(discussionId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Discussion", "id", discussionId));

        String userId = SecurityUtils.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Discussion reply = Discussion.builder()
                .courseId(courseId)
                .authorId(userId)
                .authorName(user.getName())
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .parentId(discussionId)
                .build();

        Discussion savedReply = discussionRepository.save(reply);
        return DiscussionResponse.fromEntity(savedReply);
    }

    @Override
    public void deleteDiscussion(String courseId, String discussionId) {
        Discussion discussion = discussionRepository.findByIdAndCourseId(discussionId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Discussion", "id", discussionId));
        
        String currentUserId = SecurityUtils.getCurrentUserId();
        if (!discussion.getAuthorId().equals(currentUserId) && !SecurityUtils.hasRole("ADMIN")) {
            throw new AccessDeniedException("You are not authorized to delete this discussion");
        }
        
        // Delete all replies first
        discussionRepository.deleteByParentId(discussionId);
        discussionRepository.delete(discussion);
    }

    private DiscussionResponse mapToResponseWithReplies(Discussion discussion) {
        DiscussionResponse response = DiscussionResponse.fromEntity(discussion);
        if (discussion.getParentId() == null) { // Only load replies for top-level discussions
            List<DiscussionResponse> replies = discussionRepository.findByParentIdOrderByCreatedAtAsc(discussion.getId())
                    .stream()
                    .map(DiscussionResponse::fromEntity)
                    .collect(Collectors.toList());
            response.setReplies(replies);
        }
        return response;
    }
}