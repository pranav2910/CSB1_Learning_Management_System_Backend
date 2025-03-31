package com.lms.service.impl;

import com.lms.dto.response.CertificateResponse;
import com.lms.exception.ResourceNotFoundException;
import com.lms.model.*;
import com.lms.repository.*;
import com.lms.security.SecurityUtils;
import com.lms.service.CertificateService;
import com.lms.service.PdfGenerationService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@Service
@RequiredArgsConstructor
public class CertificateServiceImpl implements CertificateService {
    private final CertificateRepository certificateRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final PdfGenerationService pdfGenerationService;

    @Override
    public CertificateResponse generateCertificate(String courseId) throws ResourceNotFoundException {
        String studentId = SecurityUtils.getCurrentUserId();
        
        Enrollment enrollment = enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment", "student and course", studentId + "-" + courseId));
        
        if (enrollment.getProgress() < 100) {
            throw new IllegalStateException("Course not completed");
        }
        
        if (certificateRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
            throw new IllegalStateException("Certificate already exists");
        }
        
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));
        
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", studentId));
        
        String verificationCode = UUID.randomUUID().toString();
        String pdfUrl;
        try {
            pdfUrl = pdfGenerationService.generateCertificatePdf(student, course, verificationCode);
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate certificate PDF", e);
        }
        
        Certificate certificate = Certificate.builder()
                .studentId(studentId)
                .courseId(courseId)
                .verificationCode(verificationCode)
                .pdfUrl(pdfUrl)
                .build(); 
        
        Certificate savedCertificate = certificateRepository.save(certificate);
        return toResponse(savedCertificate, student, course);
    }

    @Override
    public byte[] generateCertificatePdf(String certificateId) throws ResourceNotFoundException {
        Certificate certificate = certificateRepository.findById(certificateId)
                .orElseThrow(() -> new ResourceNotFoundException("Certificate", "id", certificateId));
        
                try {
                    return pdfGenerationService.getCertificatePdf(certificate.getPdfUrl());
                } catch (IOException e) {
                    throw new RuntimeException("Failed to retrieve certificate PDF", e);
                }
    }

    @Override
    public List<CertificateResponse> getUserCertificates() throws ResourceNotFoundException {
        String studentId = SecurityUtils.getCurrentUserId();
        List<Certificate> certificates = certificateRepository.findByStudentId(studentId);
        
        return certificates.stream()
                .map(cert -> {
                    Course course = courseRepository.findById(cert.getCourseId())
                            .orElseThrow(() -> new ResourceNotFoundException("Course", "id", cert.getCourseId()));
                    User student = userRepository.findById(studentId)
                            .orElseThrow(() -> new ResourceNotFoundException("User", "id", studentId));
                    return toResponse(cert, student, course);
                })
                .collect(Collectors.toList());
    }

    @Override
    public CertificateResponse getCertificateForCourse(String courseId) throws ResourceNotFoundException {
        String studentId = SecurityUtils.getCurrentUserId();
        
        Certificate certificate = certificateRepository.findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Certificate", 
                    "studentId and courseId", 
                    studentId + "-" + courseId
                ));
        
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));
        
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", studentId));
        
        return toResponse(certificate, student, course);
    }

    private CertificateResponse toResponse(Certificate certificate, User student, Course course) {
        return CertificateResponse.builder()
                .id(certificate.getId())
                .courseId(certificate.getCourseId())
                .courseTitle(course.getTitle())
                .studentName(student.getName())
                .verificationCode(certificate.getVerificationCode())
                .issueDate(certificate.getIssuedAt())
                .pdfUrl(certificate.getPdfUrl())
                .build();
    }
}