package com.integrador.svfapi.service;

import com.integrador.svfapi.dto.ReportDTO;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface ReportServiceAPI {
    ReportDTO getReport(Map<String, Object> params);
}
