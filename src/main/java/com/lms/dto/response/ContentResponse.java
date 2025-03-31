package com.lms.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class ContentResponse {
    private String id;
    private String courseId;
    private String title;
    private String description;
    private String contentType;
    private String contentUrl;
    private Integer duration;
    private Integer order;
    private Boolean isFreePreview;
    private List<QuestionResponse> questions;

    @Data
    @Builder
    public static class QuestionResponse {
        private String questionText;
        private List<String> options;
        private Integer correctAnswerIndex;
    }
}