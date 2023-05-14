package com.integrador.svfapi.service;

import com.integrador.svfapi.classes.Representatives;
import com.integrador.svfapi.classes.ResponseFormat;
import com.integrador.svfapi.classes.Student;
import org.springframework.http.ResponseEntity;

public interface StudentService {
    ResponseEntity<ResponseFormat> studentInformation(String token);
    ResponseEntity<ResponseFormat> getAllStudents();
    ResponseEntity<ResponseFormat> getStudentById(String studentCod);
    ResponseEntity<ResponseFormat> searchStudent(String query);
    ResponseEntity<ResponseFormat> addStudent(Student student, Representatives representatives);
    ResponseEntity<ResponseFormat> updateStudent(String studentCod);
    ResponseEntity<ResponseFormat> deleteStudent(String studentCod);

}
