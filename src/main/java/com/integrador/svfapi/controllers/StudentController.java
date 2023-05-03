package com.integrador.svfapi.controllers;

import com.integrador.svfapi.service.StudentService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/enrollment")
public class StudentController {

    private final StudentService studentService;
    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/")
    public ResponseEntity<?> studentInformation(
            @RequestHeader("Authorization") @NotBlank String token
    ) {
        if(!token.startsWith("Bearer ")) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        token = token.replace("Bearer ", "");
        return studentService.studentInformation(token);
    }
}
