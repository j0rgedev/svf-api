package com.integrador.svfapi.repository;

import com.integrador.svfapi.classes.EnrollmentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentDetailsRepository extends JpaRepository<EnrollmentDetails,String> {
}
