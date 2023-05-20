package com.integrador.svfapi.controllers;

import com.integrador.svfapi.dto.addStudentBody.AddStudentBodyDTO;
import com.integrador.svfapi.dto.addStudentBody.RepresentativeInfoDTO;
import com.integrador.svfapi.dto.addStudentBody.StudentInfoDTO;
import com.integrador.svfapi.dto.updateStudentBody.UpdateStudentInfoDTO;
import com.integrador.svfapi.service.impl.StudentServiceImpl;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final StudentServiceImpl studentServiceImpl;

     @Autowired
    public AdminController(StudentServiceImpl studentServiceImpl) {
        this.studentServiceImpl = studentServiceImpl;
    }

    @PostMapping("/student/add") // Endpoint to add student
    public ResponseEntity<?> addStudent(
            @RequestHeader("Authorization") @NotBlank String token,
            @RequestBody AddStudentBodyDTO addStudentBodyDTO
            ) {
        // Check if the token is valid
        if(!token.startsWith("Bearer ")) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        token = token.replace("Bearer ", "");
        return studentServiceImpl.addStudent(token, addStudentBodyDTO);
    }

    @PutMapping("/student/update/{studentCod}") // Endpoint to update a student
    public ResponseEntity<?> updateStudent(
            @RequestHeader("Authorization") @NotBlank String token,
            @PathVariable String studentCod,
            @RequestBody UpdateStudentInfoDTO updateStudentInfoDTO
            ) {
        // Check if the token is valid
        if(!token.startsWith("Bearer ")) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        token = token.replace("Bearer ", "");
        return studentServiceImpl.updateStudent(token, studentCod,updateStudentInfoDTO);
    }

    @PostMapping("/student/delete/{studentCod}") // Endpoint to delete a student
    public ResponseEntity<?> deleteStudent(
            @RequestHeader("Authorization") @NotBlank String token,
            @PathVariable String studentCod
    ) {
        // Check if the token is valid
        if(!token.startsWith("Bearer ")) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        token = token.replace("Bearer ", "");
        return studentServiceImpl.deleteStudent(token, studentCod);
    }

}
