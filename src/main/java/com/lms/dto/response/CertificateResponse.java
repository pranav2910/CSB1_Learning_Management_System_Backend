package com.lms.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class CertificateResponse {
    private String id;
    private String courseId;
    private String courseTitle;
    private String studentName;
    private LocalDate issueDate;
    private String pdfUrl;  // Changed from certificateUrl to match service
    private String verificationCode;
}