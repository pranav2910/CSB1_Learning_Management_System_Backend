package com.lms.repository;

import com.lms.model.Certificate;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface CertificateRepository extends MongoRepository<Certificate, String> {
    
    // Find a certificate by studentId and courseId (used in getCertificateForCourse())
    Optional<Certificate> findByStudentIdAndCourseId(String studentId, String courseId);

    // Check if a certificate exists (used in generateCertificate())
    boolean existsByStudentIdAndCourseId(String studentId, String courseId);

    // List all certificates for a user (used in getUserCertificates())
    List<Certificate> findByStudentId(String studentId);
}