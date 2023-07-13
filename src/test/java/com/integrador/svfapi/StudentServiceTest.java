package com.integrador.svfapi;

import com.integrador.svfapi.classes.ResponseFormat;
import com.integrador.svfapi.classes.Student;
import com.integrador.svfapi.classes.User;
import com.integrador.svfapi.repository.*;
import com.integrador.svfapi.service.impl.StudentServiceImpl;
import com.integrador.svfapi.utils.*;
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
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class StudentServiceTest {

    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private JMail jMail;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private RepresentativesRepository representativesRepository;
    @Mock
    private ReceiptRepository receiptRepository;
    @Mock
    private ReceiptPensionRepository receiptPensionRepository;
    @Mock
    private PaymentsRepository paymentsRepository;
    @Mock
    private StudentRepresentativesRepository studentRepresentativesRepository;
    @Mock
    private EnrollmentRepository enrollmentRepository;
    @Mock
    private PensionRepository pensionRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CodeGenerator codeGenerator;
    @Mock
    private PasswordEncryption passwordEncryption;
    @InjectMocks
    private StudentServiceImpl studentService;

    @Test
    void getStudentByID_WithValidToken_ReturnsStudentData(){

        String accessToken = "mockAccessToken";
        String studentCod = "studentCod";
        TokenValidationResult tokenValidationResult = new TokenValidationResult(
                true, "code", TokenType.ADMIN);

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
                true,
                new User("userId", true, "roleCode"));

        Mockito.when(jwtUtil.validateToken(accessToken)).thenReturn(tokenValidationResult);
        Mockito.when(studentRepository.findById(Mockito.anyString())).thenReturn(Optional.of(new Student()));
        Mockito.when(studentService.getStudentById(accessToken, studentCod)).thenReturn(
                ResponseEntity.ok().body(new ResponseFormat(
                        HttpStatus.OK.value(),
                        HttpStatus.OK.getReasonPhrase(),
                        null)));

        ResponseEntity<ResponseFormat> expected = ResponseEntity.ok().body(new ResponseFormat(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                null));

        ResponseEntity<ResponseFormat> result = studentService.getStudentById(accessToken, studentCod);

        Assertions.assertEquals(expected, result);

    }

}
