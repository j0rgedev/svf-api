package com.integrador.svfapi.controllers;

import com.integrador.svfapi.dto.EnrollmentDTO;
import com.integrador.svfapi.service.EnrollmentService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/enrollment/students")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @Autowired
    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping("/details")
    public ResponseEntity<?> enrollmentDetails(){
        return enrollmentService.enrollmentDetails();
    }

    @PostMapping("/process")
    public ResponseEntity<?> enrollmentProcess(
            @RequestHeader("Authorization") @NotBlank String token,
            @NotBlank @NotNull @RequestBody() EnrollmentDTO enrollmentDTO
    ){
        return enrollmentService.enrollmentProcess(token);
    }
}
