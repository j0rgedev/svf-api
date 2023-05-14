package com.integrador.svfapi.service.interfaces;

import com.integrador.svfapi.classes.ResponseFormat;
import com.integrador.svfapi.dto.enrollmentProcessBody.EnrollmentDTO;
import org.springframework.http.ResponseEntity;

public interface EnrollmentService {

    ResponseEntity<ResponseFormat> enrollmentDetails();
    ResponseEntity<ResponseFormat> enrollmentProcess(String token, EnrollmentDTO enrollmentDTO);
}
