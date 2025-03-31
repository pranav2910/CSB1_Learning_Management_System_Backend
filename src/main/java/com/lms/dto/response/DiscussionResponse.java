package com.lms.dto.response;
import com.lms.model.Discussion;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class DiscussionResponse {
    private String id;
    private String courseId;
    private String authorId;
    private String authorName;
    private String content;
    private LocalDateTime createdAt;
    private String parentId;
    private List<DiscussionResponse> replies;

    public static DiscussionResponse fromEntity(Discussion discussion) {
        return DiscussionResponse.builder()
                .id(discussion.getId())
                .courseId(discussion.getCourseId())
                .authorId(discussion.getAuthorId())
                .authorName(discussion.getAuthorName())
                .content(discussion.getContent())
                .createdAt(discussion.getCreatedAt())
                .parentId(discussion.getParentId())
                .build();
    }
}