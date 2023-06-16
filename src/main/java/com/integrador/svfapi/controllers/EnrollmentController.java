package com.integrador.svfapi.controllers;

import com.integrador.svfapi.dto.enrollmentProcessBody.EnrollmentDTO;
import com.integrador.svfapi.service.impl.EnrollmentServiceImpl;
import com.integrador.svfapi.service.impl.StudentServiceImpl;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/enrollment")
public class EnrollmentController {

    private final EnrollmentServiceImpl enrollmentServiceIMPL;
    private final StudentServiceImpl studentServiceImpl;

    @Autowired
    public EnrollmentController(EnrollmentServiceImpl enrollmentServiceIMPL, StudentServiceImpl studentServiceImpl) {
        this.enrollmentServiceIMPL = enrollmentServiceIMPL;
        this.studentServiceImpl = studentServiceImpl;
    }

    @PostMapping("/student-info") // Endpoint for student information
    public ResponseEntity<?> studentInformation(
            @RequestHeader("Authorization") @NotBlank String token
    ) {
        // Check if the token is valid
        if(!token.startsWith("Bearer ")) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        token = token.replace("Bearer ", "");
        return studentServiceImpl.studentInformation(token);
    }

    @GetMapping("/details")
    public ResponseEntity<?> enrollmentDetails(){
        return enrollmentServiceIMPL.enrollmentDetails();
    }

    @PostMapping("/process")
    public ResponseEntity<?> enrollmentProcess(
            @RequestHeader("Authorization") @NotBlank String token,
            @Validated @RequestBody() EnrollmentDTO enrollmentDTO
    ){
        if(!token.startsWith("Bearer ")) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        token = token.replace("Bearer ", "");
        return enrollmentServiceIMPL.enrollmentProcess(token, enrollmentDTO);
    }
}
