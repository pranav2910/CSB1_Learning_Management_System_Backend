package com.lms.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Data
@Builder
@Document(collection = "certificates")
public class Certificate {
    @Id
    private String id;
    private String studentId;
    private String courseId;
    private String verificationCode;
    private String pdfUrl;
    @Builder.Default
    private LocalDate issuedAt = LocalDate.now();
}