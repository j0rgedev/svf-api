package com.integrador.svfapi.service;

import com.integrador.svfapi.classes.ResponseFormat;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface StudentServiceInterface {
    ResponseEntity<ResponseFormat> getAllStudents();
    ResponseEntity<ResponseFormat> getStudentById(String studentCod);
    ResponseEntity<ResponseFormat> studentInformation(String token);
    String[] calculateNewLevelAndGrade(char currentGrade, String currentLevel);
}
