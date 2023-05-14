package com.integrador.svfapi.service;

import com.integrador.svfapi.classes.ResponseFormat;
import org.springframework.http.ResponseEntity;

public interface StudentService {
    ResponseEntity<ResponseFormat> studentInformation(String token);
    ResponseEntity<ResponseFormat> getAllStudents();
    ResponseEntity<ResponseFormat> getStudentById(String studentCod);


}
