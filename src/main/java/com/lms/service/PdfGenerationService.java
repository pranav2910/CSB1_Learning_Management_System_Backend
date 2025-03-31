package com.lms.service;

import com.lms.model.User;
import com.lms.model.Course;
import java.io.IOException;
import java.nio.file.Path;

@SuppressWarnings("unused")
public interface PdfGenerationService {
    String generateCertificatePdf(User student, Course course, String verificationCode) throws IOException;
    byte[] getCertificatePdf(String pdfUrl) throws IOException;
}