package com.integrador.svfapi;

import com.integrador.svfapi.classes.ResponseFormat;
import com.integrador.svfapi.classes.Student;
import com.integrador.svfapi.classes.User;
import com.integrador.svfapi.dto.dashboardDTO.EnrollmentCountDTO;
import com.integrador.svfapi.dto.dashboardDTO.GeneralStatistics;
import com.integrador.svfapi.dto.dashboardDTO.LastEnrolledStudentsDTO;
import com.integrador.svfapi.dto.dashboardDTO.MonthPensionsCount;
import com.integrador.svfapi.repository.PensionRepository;
import com.integrador.svfapi.repository.StudentRepository;
import com.integrador.svfapi.service.impl.StatisticsServiceImpl;
import com.integrador.svfapi.utils.JwtUtil;
import com.integrador.svfapi.utils.TokenType;
import com.integrador.svfapi.utils.TokenValidationResult;
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
import java.util.ArrayList;
import java.util.List;

class StatisticsServiceTest {

    @Mock
    private  StudentRepository studentRepository;
    @Mock
    private  PensionRepository pensionRepository;
    @Mock
    private  JwtUtil jwtUtil;
    @InjectMocks
    private StatisticsServiceImpl statisticsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("Obtener las estadísticas generales del administrador")
    @Test
    void getGeneralStatistics_WithValidToken_ANDMonthDefault(){

        String accessToken = "mockAccessToken";
        int monthNumber = 0; // Indica que se debe obtener las estadísticas generales de todos los meses

        List<Object[]> mockPensionsQuantity = new ArrayList<>();
        mockPensionsQuantity.add(new Object[]{3, 300L});
        mockPensionsQuantity.add(new Object[]{4, 300L});
        mockPensionsQuantity.add(new Object[]{5, 300L});

        List<MonthPensionsCount> returnMockPensionsQuantity = new ArrayList<>();
        returnMockPensionsQuantity.add(new MonthPensionsCount(3, 300L));
        returnMockPensionsQuantity.add(new MonthPensionsCount(4, 300L));
        returnMockPensionsQuantity.add(new MonthPensionsCount(5, 300L));

        List<LastEnrolledStudentsDTO> mockLastFiveEnrolledStudents = new ArrayList<>();
        List<Student> mockActiveStudents = new ArrayList<>();

        LastEnrolledStudentsDTO mockLastEnrolledStudentsDTO = new LastEnrolledStudentsDTO(
                "studentCod",
                "names lastNames",
                "currentLevel"
        );

        mockLastFiveEnrolledStudents.add(mockLastEnrolledStudentsDTO);
        mockLastFiveEnrolledStudents.add(mockLastEnrolledStudentsDTO);
        mockLastFiveEnrolledStudents.add(mockLastEnrolledStudentsDTO);

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

        mockActiveStudents.add(student);
        mockActiveStudents.add(student);
        mockActiveStudents.add(student);

        EnrollmentCountDTO mockEnrollmentCountDTO = new EnrollmentCountDTO(3,0,3);

        GeneralStatistics mockGeneralStatistics = new GeneralStatistics(
                returnMockPensionsQuantity,
                mockLastFiveEnrolledStudents,
                mockEnrollmentCountDTO
        );

        // Validar el token
        Mockito.when(jwtUtil.validateToken(Mockito.anyString()))
                .thenReturn(new TokenValidationResult(true, "code", TokenType.ADMIN));

        Mockito.when(pensionRepository.getPensionsQuantity())
                .thenReturn(mockPensionsQuantity);

        Mockito.when(studentRepository.getLastFiveEnrolledStudents())
                .thenReturn(mockActiveStudents);

        Mockito.when(studentRepository.findActiveStudents())
                .thenReturn(mockActiveStudents);

        ResponseEntity<ResponseFormat> response = statisticsService.getGeneralStatistics(accessToken, monthNumber);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(mockGeneralStatistics, response.getBody().data());

        System.out.println("Test de obtener las estadísticas generales del administrador exitoso");

    }

}
