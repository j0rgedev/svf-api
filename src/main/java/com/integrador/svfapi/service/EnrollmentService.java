package com.integrador.svfapi.service;

import com.integrador.svfapi.classes.ResponseFormat;
import com.integrador.svfapi.dto.enrollmentProcessBody.EnrollmentDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface EnrollmentService {

    ResponseEntity<ResponseFormat> enrollmentDetails();
    ResponseEntity<ResponseFormat> enrollmentProcess(String token, EnrollmentDTO enrollmentDTO);
}
