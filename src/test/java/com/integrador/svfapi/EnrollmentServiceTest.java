package com.integrador.svfapi;

import com.integrador.svfapi.classes.Enrollment;
import com.integrador.svfapi.classes.Pension;
import com.integrador.svfapi.classes.ResponseFormat;
import com.integrador.svfapi.classes.Student;
import com.integrador.svfapi.dto.PaymentDTO;
import com.integrador.svfapi.dto.enrollmentProcessBody.EnrollmentDTO;
import com.integrador.svfapi.dto.enrollmentProcessBody.LevelDetailsDTO;
import com.integrador.svfapi.repository.*;
import com.integrador.svfapi.service.impl.EnrollmentServiceImpl;
import com.integrador.svfapi.utils.JwtUtil;
import com.integrador.svfapi.utils.TokenType;
import com.integrador.svfapi.utils.TokenValidationResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class EnrollmentServiceTest {

    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private EnrollmentRepository enrollmentRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private PensionRepository pensionRepository;
    @Mock
    private TermsAndConditionsRepository termsAndConditionsRepository;
    @Mock
    private TermsDetailsRepository termsDetailsRepository;
    @Mock
    private LevelCostsRepository levelCostsRepository;
    @InjectMocks
    private EnrollmentServiceImpl enrollmentService;

    @Test
    void testEnrollmentProcess_WithValidToken_ReturnsEnrollmentId() {
        

        String accessToken = "mockAccessToken";

        TokenValidationResult tokenValidationResult = new TokenValidationResult(
                true, "SVF1234", TokenType.STUDENT);

        Enrollment lastEnrollment = new Enrollment();
        lastEnrollment.setEnrollmentId("E00000");

        Student student = new Student();
        student.setStudentCod("SVF1234");
        student.setCurrentLevel("Primaria");

        EnrollmentDTO enrollmentDTO = new EnrollmentDTO(
                150.00,
                LocalDate.now(),
                new LevelDetailsDTO("LP2023", "Primaria"),
                new PaymentDTO("P20", "Pago Efectivo"));

        HashMap<String, String> data = new HashMap<>();
        data.put("enrollmentId", "E10000");
        // Objeto esperado
        ResponseEntity<ResponseFormat> expected = ResponseEntity.ok().body(new ResponseFormat(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                data));

        Mockito.when(jwtUtil.validateToken(accessToken)).thenReturn(tokenValidationResult);
        Mockito.when(enrollmentRepository
                .findByStudentCodAndTermsConditionsId(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(null);
        Mockito.when(enrollmentRepository.findTopByOrderByEnrollmentIdDesc())
                .thenReturn(lastEnrollment);
        Mockito.when(studentRepository.findById(Mockito.anyString())).thenReturn(Optional.of(student));
        Mockito.when(enrollmentService.enrollmentProcess(
                accessToken, enrollmentDTO))
                .thenReturn(ResponseEntity.ok().body(new ResponseFormat(
                        HttpStatus.OK.value(),
                        HttpStatus.OK.getReasonPhrase(),
                        data)));

        Mockito.doNothing().when(enrollmentRepository.saveAndFlush(Mockito.any(Enrollment.class)));

        //Objeto obtenido
        ResponseEntity<ResponseFormat> result = enrollmentService.enrollmentProcess(
                accessToken, enrollmentDTO);

        Assertions.assertEquals(expected, result);
    }
}
