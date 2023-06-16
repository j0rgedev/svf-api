package com.integrador.svfapi.service;

import com.integrador.svfapi.classes.ResponseFormat;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;

@Service
public interface ReportService {

    ResponseEntity<ResponseFormat> exportReport(String token, String reportFormat) throws FileNotFoundException, JRException;
}
