package com.integrador.svfapi.controllers;

import com.integrador.svfapi.dto.addStudentBody.AddStudentBodyDTO;
import com.integrador.svfapi.dto.updateStudentBody.UpdateStudentInfoDTO;
import com.integrador.svfapi.exception.BusinessException;
import com.integrador.svfapi.service.impl.ReportServiceImpl;
import com.integrador.svfapi.service.impl.StatisticsServiceImpl;
import com.integrador.svfapi.service.impl.StudentServiceImpl;
import jakarta.validation.constraints.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final StudentServiceImpl studentServiceImpl;
    private final StatisticsServiceImpl statisticsServiceImpl;
    private final ReportServiceImpl reportServiceImpl;

    @Autowired
    public AdminController(StudentServiceImpl studentServiceImpl, StatisticsServiceImpl statisticsServiceImpl, ReportServiceImpl reportServiceImpl) {
        this.studentServiceImpl = studentServiceImpl;
        this.statisticsServiceImpl = statisticsServiceImpl;
        this.reportServiceImpl = reportServiceImpl;
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
            @Validated @RequestBody UpdateStudentInfoDTO updateStudentInfo
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

    @PostMapping("/general-dashboard")
    public ResponseEntity<?> generalDashboard(
            @RequestHeader("Authorization") @NotBlank String token,
            @RequestParam("monthNumber") int monthNumber
    ) {
        // Check if the token is valid
        if (!token.startsWith("Bearer ")) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        token = token.replace("Bearer ", "");
        return statisticsServiceImpl.getGeneralStatistics(token, monthNumber);
    }

    @PostMapping("/enrollment-dashboard")
    public ResponseEntity<?> enrollmentDashboard(
            @RequestHeader("Authorization") @NotBlank String token
    ) {
        // Check if the token is valid
        if (!token.startsWith("Bearer ")) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        token = token.replace("Bearer ", "");
        return statisticsServiceImpl.getEnrollmentStatistics(token);
    }

    @PostMapping("/pension-dashboard")
    public ResponseEntity<?> pensionDashboard(
            @RequestHeader("Authorization") @NotBlank String token
    ) {
        // Check if the token is valid
        if (!token.startsWith("Bearer ")) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        token = token.replace("Bearer ", "");
        return statisticsServiceImpl.getPensionStatistics(token);
    }

    @PostMapping("/total-debt/{monthNumber}")
    @Validated
    public ResponseEntity<?> totalDebt(
            @RequestHeader("Authorization") @NotBlank String token,
            @PathVariable("monthNumber") int monthNumber
            ) {
        // Check if the token is valid
        if (!token.startsWith("Bearer ")) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        token = token.replace("Bearer ", "");

        if(monthNumber < 3 || monthNumber > 12) throw new BusinessException(HttpStatus.BAD_REQUEST, "El mes debe estar entre 3 y 12");

        return statisticsServiceImpl.getTotalDebt(token, monthNumber);
    }

    @PostMapping("/pension-report/{format}")
    public ResponseEntity<?> generateReport(
            @RequestHeader("Authorization") @NotBlank String token,
            @PathVariable("format") String format
    ) {
        // Check if the token is valid
        if (!token.startsWith("Bearer ")) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        token = token.replace("Bearer ", "");

        return reportServiceImpl.exportReport(token, format);
    }

    @PostMapping("/main-report")
    public ResponseEntity<Resource> download(
            @RequestHeader("Authorization") @NotBlank String token,
            @RequestParam Map<String, Object> params
    ) {
        // Check if the token is valid
        if (!token.startsWith("Bearer ")) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        token = token.replace("Bearer ", "");
        return reportServiceImpl.mainReport(token, params);
    }

    @PostMapping("/pensions-report")
    public ResponseEntity<InputStreamResource> downloadPensions(
            @RequestHeader("Authorization") @NotBlank String token
    ) {
        // Check if the token is valid
        if (!token.startsWith("Bearer ")) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        token = token.replace("Bearer ", "");
        return reportServiceImpl.pensionsReport(token);
    }

    @PostMapping("/receipt")
    public ResponseEntity<Resource> receiptDownload(
            @RequestParam Map<String, Object> params
    ) {
        return reportServiceImpl.receiptReport(params);
    }

}
