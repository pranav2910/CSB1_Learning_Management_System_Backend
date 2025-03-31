package com.lms.model;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Data
@Builder
@Document(collection = "contents")
public class Content {
    @Id
    private String id;
    private String courseId;
    private String title;
    private String description;
    private ContentType contentType;
    private String contentUrl;
    private Integer duration;
    private Integer order;
    private Boolean isFreePreview;
    private List<Question> questions;

    public enum ContentType {
        VIDEO, QUIZ, DOCUMENT
    }

    @Data
    @Builder
    public static class Question {
        private String questionText;
        private List<String> options;
        private Integer correctAnswerIndex;
    }
}