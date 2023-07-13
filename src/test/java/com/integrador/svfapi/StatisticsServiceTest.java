package com.integrador.svfapi;

import com.integrador.svfapi.classes.ResponseFormat;
import com.integrador.svfapi.dto.dashboardDTO.MonthPensionsCount;
import com.integrador.svfapi.repository.PensionRepository;
import com.integrador.svfapi.repository.StudentRepository;
import com.integrador.svfapi.service.impl.StatisticsServiceImpl;
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

import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class StatisticsServiceTest {

    @Mock
    private  StudentRepository studentRepository;
    @Mock
    private  PensionRepository pensionRepository;
    @Mock
    private  JwtUtil jwtUtil;
    @InjectMocks
    private StatisticsServiceImpl statisticsService;

    @Test
    void getGeneralStatistics_WithValidToken_ANDMonthDefault(){

        String accessToken = "mockAccessToken";
        TokenValidationResult tokenValidationResult = new TokenValidationResult(
                true, "code", TokenType.ADMIN);

        ResponseEntity<ResponseFormat> expected = ResponseEntity.ok().body(new ResponseFormat(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                null
        ));

        Mockito.when(jwtUtil.validateToken(Mockito.anyString())).thenReturn(tokenValidationResult);
        Mockito.when(pensionRepository.getPensionsQuantity()).thenReturn(new ArrayList<>());
        Mockito.when(studentRepository.getLastFiveEnrolledStudents()).thenReturn(new ArrayList<>());
        Mockito.when(studentRepository.findActiveStudents()).thenReturn(new ArrayList<>());
        Mockito.when(statisticsService.getGeneralStatistics(accessToken, 0))
                .thenReturn(ResponseEntity.ok().body(new ResponseFormat(
                        HttpStatus.OK.value(),
                        HttpStatus.OK.getReasonPhrase(),
                        null
                )));

        ResponseEntity<ResponseFormat> result = statisticsService.getGeneralStatistics(accessToken, 0);

        Assertions.assertEquals(expected, result);

    }

}
