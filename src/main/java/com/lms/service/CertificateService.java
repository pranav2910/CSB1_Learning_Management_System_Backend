package com.lms.service;

import com.lms.dto.response.CertificateResponse;
import com.lms.exception.ResourceNotFoundException;

import java.util.List;

public interface CertificateService {
    CertificateResponse generateCertificate(String courseId) throws ResourceNotFoundException;
    byte[] generateCertificatePdf(String certificateId) throws ResourceNotFoundException;
    List<CertificateResponse> getUserCertificates() throws ResourceNotFoundException;
    CertificateResponse getCertificateForCourse(String courseId) throws ResourceNotFoundException;
}