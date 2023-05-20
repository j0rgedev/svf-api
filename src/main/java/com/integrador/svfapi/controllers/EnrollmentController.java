package com.integrador.svfapi.controllers;

import com.integrador.svfapi.dto.enrollmentProcessBody.EnrollmentDTO;
import com.integrador.svfapi.service.impl.EnrollmentServiceImpl;
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

    @Autowired
    public EnrollmentController(EnrollmentServiceImpl enrollmentServiceIMPL) {
        this.enrollmentServiceIMPL = enrollmentServiceIMPL;
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
