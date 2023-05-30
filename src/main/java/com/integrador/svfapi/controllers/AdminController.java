package com.integrador.svfapi.controllers;

import com.integrador.svfapi.dto.addStudentBody.AddStudentBodyDTO;
import com.integrador.svfapi.dto.updateStudentBody.UpdateStudentInfoDTO;
import com.integrador.svfapi.service.impl.StudentServiceImpl;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final StudentServiceImpl studentServiceImpl;

    @Autowired
    public AdminController(StudentServiceImpl studentServiceImpl) {
        this.studentServiceImpl = studentServiceImpl;
    }

    @PostMapping("/students")
    public ResponseEntity<?> getAllStudentsInformation(
            @RequestHeader("Authorization") @NotBlank String token
    ) {
        if (!token.startsWith("Bearer ")) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        token = token.replace("Bearer ", "");
        return studentServiceImpl.getAllStudents(token);
    }

    @PostMapping("/student")
    public ResponseEntity<?> getStudentById(
            @RequestHeader("Authorization") @NotBlank String token,
            @RequestParam @NotBlank String studentCod
    ) {
        if (!token.startsWith("Bearer ")) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        token = token.replace("Bearer ", "");
        return studentServiceImpl.getStudentById(token, studentCod);
    }

    @PostMapping("/student/{query}")
    public ResponseEntity<?> getStudentByQuery(
            @RequestHeader("Authorization") @NotBlank String token,
            @PathVariable @NotBlank String query
    ) {
        if (!token.startsWith("Bearer ")) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        token = token.replace("Bearer ", "");
        return studentServiceImpl.getStudentByQuery(token, query);
    }

    @PostMapping("/student/add") // Endpoint to add student
    public ResponseEntity<?> addStudent(
            @RequestHeader("Authorization") @NotBlank String token,
            @Validated @RequestBody AddStudentBodyDTO addStudentBodyDTO
    ) {
        // Check if the token is valid
        if (!token.startsWith("Bearer ")) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        token = token.replace("Bearer ", "");
        return studentServiceImpl.addStudent(token, addStudentBodyDTO);
    }

    @PutMapping("/student/update/{studentCod}") // Endpoint to update a student
    public ResponseEntity<?> updateStudent(
            @RequestHeader("Authorization") @NotBlank String token,
            @PathVariable String studentCod,
            @RequestBody UpdateStudentInfoDTO updateStudentInfo
    ) {
        // Check if the token is valid
        if (!token.startsWith("Bearer ")) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        token = token.replace("Bearer ", "");
        return studentServiceImpl.updateStudent(token, studentCod, updateStudentInfo);
    }

    @PostMapping("/student/delete/{studentCod}") // Endpoint to delete a student
    public ResponseEntity<?> deleteStudent(
            @RequestHeader("Authorization") @NotBlank String token,
            @PathVariable String studentCod
    ) {
        // Check if the token is valid
        if (!token.startsWith("Bearer ")) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        token = token.replace("Bearer ", "");
        return studentServiceImpl.deleteStudent(token, studentCod);
    }

    @PostMapping("/dashboard")
    public ResponseEntity<?> dashboardGraphics(
            @RequestHeader("Authorization") @NotBlank String token
    ) {
        // Check if the token is valid
        if (!token.startsWith("Bearer ")) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        token = token.replace("Bearer ", "");
        return studentServiceImpl.dashboardGraphics();
    }

    @PostMapping("/dashboard2")
    public ResponseEntity<?> dashboardGraphics2(
            @RequestHeader("Authorization") @NotBlank String token
    ) {
        // Check if the token is valid
        if (!token.startsWith("Bearer ")) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        token = token.replace("Bearer ", "");
        return studentServiceImpl.secondGraphic();
    }
}
