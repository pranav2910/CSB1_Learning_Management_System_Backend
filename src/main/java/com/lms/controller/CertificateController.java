package com.lms.controller;

import com.lms.dto.response.CertificateResponse;
import com.lms.service.CertificateService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/certificates")
@RequiredArgsConstructor
public class CertificateController {

    private final CertificateService certificateService;

    @GetMapping("/course/{courseId}")
    public ResponseEntity<CertificateResponse> getCertificateForCourse(
            @PathVariable String courseId) {
        return ResponseEntity.ok(certificateService.getCertificateForCourse(courseId));
    }

    @GetMapping("/{certificateId}/download")
    public ResponseEntity<byte[]> downloadCertificate(
            @PathVariable String certificateId) {
        byte[] pdfBytes = certificateService.generateCertificatePdf(certificateId);
        return ResponseEntity.ok()
            .header("Content-Type", "application/pdf")
            .header("Content-Disposition", "attachment; filename=certificate.pdf")
            .body(pdfBytes);
    }

    @GetMapping("/user")
    public ResponseEntity<List<CertificateResponse>> getUserCertificates() {
        return ResponseEntity.ok(certificateService.getUserCertificates());
    }

    @PostMapping("/{courseId}/generate")
    public ResponseEntity<CertificateResponse> generateCertificate(
            @PathVariable String courseId) {
        return ResponseEntity.ok(certificateService.generateCertificate(courseId));
    }
}