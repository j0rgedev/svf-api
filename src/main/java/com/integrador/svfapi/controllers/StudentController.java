package com.integrador.svfapi.controllers;

import com.integrador.svfapi.service.impl.StudentServiceImpl;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/student")
public class StudentController {

    private final StudentServiceImpl studentServiceImpl;
    @Autowired
    public StudentController(StudentServiceImpl studentServiceImpl) {
        this.studentServiceImpl = studentServiceImpl;
    }

    /*
    * ENROLLMENT PROCESS ENDPOINTS
    * */
    @PostMapping("/enrollment-info") // Endpoint for student information
    public ResponseEntity<?> studentInformation(
            @RequestHeader("Authorization") @NotBlank String token
    ) {
        // Check if the token is valid
        if(!token.startsWith("Bearer ")) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        token = token.replace("Bearer ", "");
        return studentServiceImpl.studentInformation(token);
    }


    /*
    * INTRANET ENDPOINTS
    * */
    @PostMapping("/pensions") // Endpoint for student pensions
    public ResponseEntity<?> studentPensions(
            @RequestHeader("Authorization") @NotBlank String token
    ) {
        // Check if the token is valid
        if(!token.startsWith("Bearer ")) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        token = token.replace("Bearer ", "");
        return studentServiceImpl.studentPensions(token);
    }
}
