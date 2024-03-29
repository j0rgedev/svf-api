package com.integrador.svfapi.controllers;

import com.integrador.svfapi.dto.PensionsPayment;
import com.integrador.svfapi.service.impl.ReportServiceImpl;
import com.integrador.svfapi.service.impl.StudentServiceImpl;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/student")
public class StudentController {

    private final StudentServiceImpl studentServiceImpl;
    private final ReportServiceImpl reportServiceImpl;
    @Autowired
    public StudentController(StudentServiceImpl studentServiceImpl, ReportServiceImpl reportServiceImpl) {
        this.studentServiceImpl = studentServiceImpl;
        this.reportServiceImpl = reportServiceImpl;
    }

    /*
    * INTRANET ENDPOINTS
    * */
    @PostMapping("/")
    public ResponseEntity<?> getStudent(
            @RequestHeader("Authorization") @NotBlank String token
    ) {
        // Check if the token is valid
        if(!token.startsWith("Bearer ")) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        token = token.replace("Bearer ", "");
        return studentServiceImpl.getStudent(token);
    }
    
    @PostMapping("/pensions/{status}") // Endpoint for student pensions
    public ResponseEntity<?> studentPensions(
            @RequestHeader("Authorization") @NotBlank String token,
            @PathVariable("status") @NotBlank boolean status
    ) {
        // Check if the token is valid
        if(!token.startsWith("Bearer ")) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        token = token.replace("Bearer ", "");
        return studentServiceImpl.studentPensions(token, status);
    }

    @PostMapping("/pensions/pay") // Endpoint for student pensions
    public ResponseEntity<?> payPension(
            @RequestHeader("Authorization") @NotBlank String token,
            @RequestBody @Validated PensionsPayment pensionsPayment
            ) {
        // Check if the token is valid
        if(!token.startsWith("Bearer ")) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        token = token.replace("Bearer ", "");
        return studentServiceImpl.payPension(token, pensionsPayment);
    }

    @PostMapping("/receipt")
    public ResponseEntity<Resource> receiptDownload(
            @RequestHeader("Authorization") @NotBlank String token,
            @RequestParam Map<String, Object> params
    ) {
        // Check if the token is valid
        if(!token.startsWith("Bearer ")) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        token = token.replace("Bearer ", "");
        return reportServiceImpl.receiptReport(params);
    }
}
