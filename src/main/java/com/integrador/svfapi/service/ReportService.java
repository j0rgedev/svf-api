package com.integrador.svfapi.service;

import com.integrador.svfapi.classes.ResponseFormat;
import com.integrador.svfapi.dto.ReportDTO;
import net.sf.jasperreports.engine.JRException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.Map;

@Service
public interface ReportService {

    ResponseEntity<ResponseFormat> exportReport(String token, String reportFormat);
    ResponseEntity<Resource> mainReport(String token, Map<String, Object> params);
    ResponseEntity<InputStreamResource> pensionsReport(String token);

    ResponseEntity<Resource> receiptReport(Map<String, Object> params);
}
