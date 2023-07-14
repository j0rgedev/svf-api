package com.integrador.svfapi;

import com.integrador.svfapi.classes.Enrollment;
import com.integrador.svfapi.classes.Pension;
import com.integrador.svfapi.classes.ResponseFormat;
import com.integrador.svfapi.classes.Student;
import com.integrador.svfapi.controllers.EnrollmentController;
import com.integrador.svfapi.dto.PaymentDTO;
import com.integrador.svfapi.dto.enrollmentProcessBody.EnrollmentDTO;
import com.integrador.svfapi.dto.enrollmentProcessBody.LevelDetailsDTO;
import com.integrador.svfapi.repository.*;
import com.integrador.svfapi.service.impl.EnrollmentServiceImpl;
import com.integrador.svfapi.service.impl.StudentServiceImpl;
import com.integrador.svfapi.utils.JwtUtil;
import com.integrador.svfapi.utils.TokenType;
import com.integrador.svfapi.utils.TokenValidationResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

class EnrollmentServiceTest {

    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private EnrollmentRepository enrollmentRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private PensionRepository pensionRepository;

    @InjectMocks
    private EnrollmentServiceImpl enrollmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("Creación de la matrícula y pensiones de un estudiante")
    @Test
    void createEnrollment() {

        String token = "student_token";
        String studentId = "SVF0007";
        Student student = new Student();
        student.setStudentCod(studentId);
        student.setCurrentLevel("Primaria");

        EnrollmentDTO enrollmentDTO = new EnrollmentDTO(
                150.00,
                LocalDate.now(),
                new LevelDetailsDTO("LI23", "Inicial"),
                new PaymentDTO("P10", "Tarjeta de crédito o débito")
        );

        Pension pension = new Pension(
                1,
                LocalDate.now(),
                350.00,
                false,
                student
        );

        // Validación del token
        Mockito.when(jwtUtil.validateToken(token))
                .thenReturn(new TokenValidationResult(true, studentId, TokenType.STUDENT));

        // Validación de que el estudiante no tenga una matrícula activa
        Mockito.when(enrollmentRepository.findByStudentCodAndTermsConditionsId(studentId, "T2023"))
                .thenReturn(null);

        // Obtiene la última matrícula registrada
        String lastEnrollmentId = "E00001";
        Enrollment lastEnrollment = new Enrollment();
        lastEnrollment.setEnrollmentId(lastEnrollmentId);
        Mockito.when(enrollmentRepository.findTopByOrderByEnrollmentIdDesc()).thenReturn(lastEnrollment);

        // Creación de la nueva matrícula
        String newEnrollmentId = "E00002";
        Mockito.when(enrollmentRepository.saveAndFlush(Mockito.any(Enrollment.class)))
                .thenReturn(new Enrollment(newEnrollmentId, studentId, "P10", true, LocalDate.now(), 150.00, "T2023"));

        // Obtener el estudiante
        Mockito.when(studentRepository.findById(studentId))
                .thenReturn(java.util.Optional.of(student));

        // Creación de la nueva pensión
        Mockito.when(pensionRepository.saveAndFlush(Mockito.any(Pension.class)))
                .thenReturn(pension);

        // Ejecutar el método que se está probando
        ResponseEntity<ResponseFormat> response = enrollmentService.enrollmentProcess(token, enrollmentDTO);

        // Verificar que se haya creado una nueva inscripción
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());

        Map<String, Object> data = (HashMap<String, Object>) response.getBody().data();
        Assertions.assertNotNull(data);
        Assertions.assertEquals(newEnrollmentId, data.get("enrollmentId"));
        System.out.println("Test de creación de matrícula exitoso");
    }
}
