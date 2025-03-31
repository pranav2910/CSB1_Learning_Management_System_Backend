package com.lms.model;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "discussions")
public class Discussion {
    @Id
    private String id;
    private String courseId;
    private String authorId;
    private String authorName;
    private String content;
    private LocalDateTime createdAt;
    private String parentId; // Null for top-level discussions
    private boolean edited;
    private LocalDateTime editedAt;
}