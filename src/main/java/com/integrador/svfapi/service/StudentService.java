package com.integrador.svfapi.service;

import com.integrador.svfapi.dto.StudentDTO;
import com.integrador.svfapi.exception.BusinessException;
import com.integrador.svfapi.repository.StudentRepository;
import com.integrador.svfapi.classes.Student;
import com.integrador.svfapi.utils.AESEncryption;
import com.integrador.svfapi.utils.PasswordEncryption;
import com.integrador.svfapi.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

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
            return ResponseEntity.ok().body(Map.of("accessToken", key));
        } else {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "Login failed");
        }
    }

    private boolean validatePasswordFormat(String studentCod ){

        Student student = studentRepository.getReferenceById(studentCod);

        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        int currentYear = calendar.get(Calendar.YEAR);
        String format = student.getNames() + student.getDni() + currentYear;
        String defaultPasswordFormat = PasswordEncryption.generateSecurePassword(format, student.getSalt());

        return defaultPasswordFormat.equals(student.getPassword());
    }
    public ResponseEntity <Map<String, String>> updatePassword (StudentDTO studentDTO) {

        if (validatePasswordFormat(studentDTO.getStudentCod())) {

            String key= jwtUtil.generateToken(studentDTO);
            if(key == null) {
                throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "Error generating token");
            }
            return ResponseEntity.ok().body(Map.of("accessToken", key));
        } else {
           throw new BusinessException(HttpStatus.UNAUTHORIZED, "Login failed");
        }
    }


}
