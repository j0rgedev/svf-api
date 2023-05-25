package com.integrador.svfapi.service;

import com.integrador.svfapi.classes.ResponseFormat;
import com.integrador.svfapi.dto.addStudentBody.AddStudentBodyDTO;
import com.integrador.svfapi.dto.addStudentBody.RepresentativeInfoDTO;
import com.integrador.svfapi.dto.addStudentBody.StudentInfoDTO;
import com.integrador.svfapi.dto.updateStudentBody.UpdateStudentInfoDTO;
import org.springframework.http.ResponseEntity;

public interface StudentService {
    ResponseEntity<ResponseFormat> studentInformation(String token);
    // CRUD for Students
    ResponseEntity<ResponseFormat> getAllStudents();
    ResponseEntity<ResponseFormat> getStudentById(String studentCod);
    ResponseEntity<ResponseFormat> getStudentByQuery(String token, String query);
    ResponseEntity<ResponseFormat> addStudent(String token, AddStudentBodyDTO addStudentBodyDTO);
    ResponseEntity<ResponseFormat> updateStudent(String token, String studentCod, UpdateStudentInfoDTO updateStudentInfoDTO);
    ResponseEntity<ResponseFormat> deleteStudent(String token, String studentCod);

}
