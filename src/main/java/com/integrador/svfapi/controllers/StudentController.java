package com.integrador.svfapi.controllers;

import com.integrador.svfapi.service.StudentServiceImpl;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/enrollment")
public class StudentController {

    private final StudentServiceImpl studentServiceImpl;
    @Autowired
    public StudentController(StudentServiceImpl studentServiceImpl) {
        this.studentServiceImpl = studentServiceImpl;
    }

    /*
    * ENROLLMENT PROCESS ENDPOINTS
    * */
    @PostMapping("/") // Endpoint for student information
    public ResponseEntity<?> studentInformation(
            @RequestHeader("Authorization") @NotBlank String token
    ) {
        // Check if the token is valid
        if(!token.startsWith("Bearer ")) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        token = token.replace("Bearer ", "");
        return studentServiceImpl.studentInformation(token);
    }

    @PostMapping("/allStudentsInformation")
    public ResponseEntity<?> getAllStudentsInformation(
            @RequestHeader("Authorization") @NotBlank String token
    ) {
        if(!token.startsWith("Bearer ")) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        token = token.replace("Bearer ", "");
        return studentServiceImpl.getAllStudents();
    }

    @PostMapping("/getStudentById")
    public ResponseEntity<?> getStudentById(
            @RequestHeader("Authorization") @NotBlank String token,
            @RequestParam @NotBlank String studentCod
    ) {
        if(!token.startsWith("Bearer ")) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        token = token.replace("Bearer ", "");
        return studentServiceImpl.getStudentById(studentCod);
    }

}
