package com.integrador.svfapi;

import com.integrador.svfapi.classes.ResponseFormat;
import com.integrador.svfapi.classes.Student;
import com.integrador.svfapi.classes.User;
import com.integrador.svfapi.dto.getAllStudents.SingleStudentDTO;
import com.integrador.svfapi.repository.*;
import com.integrador.svfapi.service.impl.StudentServiceImpl;
import com.integrador.svfapi.utils.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Optional;

class StudentServiceTest {

    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("Obtener un estudiante por su código de estudiante")
    @Test
    void getStudentByID_WithValidToken_ReturnsStudentData(){

        String accessToken = "mockAccessToken";
        String adminCode = "ADM0001";

        Student student = new Student(
               "studentCod",
                "names",
                "lastNames",
                LocalDate.now(),
                "password",
                "salt",
                "dni",
                'G',
                "address",
                "email",
                "phone",
                'G',
                "currentLevel",
                false,
                new User("userId", true, "roleCode"));

        SingleStudentDTO singleStudentDTO = new SingleStudentDTO(
                student.getStudentCod(),
                student.getNames(),
                student.getLastNames(),
                student.getBirthday(),
                student.getDni(),
                student.getAddress(),
                student.getEmail(),
                student.getPhone(),
                student.getCurrentLevel(),
                student.getCurrentGrade()
        );

        Mockito.when(jwtUtil.validateToken(accessToken))
                .thenReturn(new TokenValidationResult(true, adminCode, TokenType.ADMIN));

        // Validar que el estudiante exista
        Mockito.when(studentRepository.findById(adminCode))
                .thenReturn(Optional.of(student));

        ResponseEntity<ResponseFormat> response = studentService.getStudentById(accessToken, adminCode);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(singleStudentDTO, response.getBody().data());
        System.out.println("Test de obtener un estudiante por su código de estudiante exitoso");

    }

}
