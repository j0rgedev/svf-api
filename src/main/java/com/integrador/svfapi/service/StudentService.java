package com.integrador.svfapi.service;

import com.integrador.svfapi.dto.StudentDTO;
import com.integrador.svfapi.exception.BusinessException;
import com.integrador.svfapi.repository.StudentRepository;
import com.integrador.svfapi.classes.Student;
import com.integrador.svfapi.utils.PasswordEncryption;
import com.integrador.svfapi.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StudentService {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private StudentRepository studentRepository;

    private final PasswordEncryption passwordEncryption = new PasswordEncryption();

    public ResponseEntity<Map<String, String>> login (StudentDTO studentDTO) {

        List<Student> allStudents = studentRepository.findAll();
        Student matchStudent = null;

        for (Student student : allStudents) {
            if (studentDTO.getStudentCod().equals(student.getStudentCod())) {
                matchStudent = student;
                break;
            }
        }

        if(matchStudent == null) {
            throw new BusinessException(HttpStatus.NOT_FOUND, "Student not found");
        }

        if (passwordEncryption.verifyUserPassword(
                studentDTO.getPassword(),
                matchStudent.getPassword(),
                matchStudent.getSalt())) {
            String key= jwtUtil.generateToken(studentDTO);
            if(key == null) {
                throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "Error generating token");
            }
            return ResponseEntity.ok().body(Map.of("token", key));
        } else {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "Login failed");
        }
    }

}
