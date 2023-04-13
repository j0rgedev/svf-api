package com.integrador.svfapi.Controllers;

import com.integrador.svfapi.Classes.StudentLogin;
import com.integrador.svfapi.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import com.integrador.svfapi.Classes.Student;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/enrollment/students")
public class LoginController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Validated @RequestBody StudentLogin studentLogin
    ) {
        return ResponseEntity.ok("Hola");
    }
}
