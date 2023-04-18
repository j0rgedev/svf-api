package com.integrador.svfapi.controllers;

import com.integrador.svfapi.dto.StudentDTO;
import com.integrador.svfapi.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/enrollment/students")
public class AuthController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Validated @RequestBody StudentDTO studentDTO
    ) {
        return studentService.login(studentDTO);
    }
}
