package com.integrador.svfapi.service;

import com.integrador.svfapi.classes.ResponseFormat;
import org.springframework.http.ResponseEntity;

public interface StatisticsService {
    ResponseEntity<ResponseFormat> getGeneralStatistics(String token);
    ResponseEntity<ResponseFormat> getEnrollmentStatistics(String token);
    ResponseEntity<ResponseFormat> getPensionStatistics(String token);
    ResponseEntity<ResponseFormat> getTotalDebt(String token, int month);
}
