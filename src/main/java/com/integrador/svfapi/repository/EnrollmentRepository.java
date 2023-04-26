package com.integrador.svfapi.repository;

import com.integrador.svfapi.classes.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment,String> {
    Enrollment findTopByOrderByEnrollmentIdDesc();
    Enrollment findByStudentCodAndTermsConditionsId(String studentCod, String termsConditionsId);
}
