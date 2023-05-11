package com.integrador.svfapi.controllers;

import com.integrador.svfapi.dto.enrollmentProcessBody.EnrollmentDTO;
import com.integrador.svfapi.service.EnrollmentService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/enrollment")
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
            @Validated @RequestBody() EnrollmentDTO enrollmentDTO
    ){
        if(!token.startsWith("Bearer ")) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        token = token.replace("Bearer ", "");
        return enrollmentService.enrollmentProcess(token, enrollmentDTO);
    }
}
