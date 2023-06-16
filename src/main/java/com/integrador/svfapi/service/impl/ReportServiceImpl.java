package com.integrador.svfapi.service.impl;

import com.integrador.svfapi.classes.ResponseFormat;
import com.integrador.svfapi.dto.StudentReportData;
import com.integrador.svfapi.exception.BusinessException;
import com.integrador.svfapi.repository.StudentRepository;
import com.integrador.svfapi.service.ReportService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    private final StudentRepository studentRepository;

    @Autowired
    public ReportServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public ResponseEntity<ResponseFormat> exportReport(String token, String reportFormat) {
        List<StudentReportData> studentList = studentRepository.getStudentsReportData();
        //Cargar archivo y compilar
        try {
            File file = ResourceUtils.getFile("classpath:students.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(studentList);
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("createdBy", "Ives");
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            String downloadsDirPath = System.getProperty("user.home") + "/Downloads";

            if (reportFormat.equalsIgnoreCase("html")) {
                JasperExportManager.exportReportToHtmlFile(jasperPrint, downloadsDirPath + "/reporte.html");
            }
            if (reportFormat.equalsIgnoreCase("pdf")) {
                JasperExportManager.exportReportToPdfFile(jasperPrint, downloadsDirPath + "/reporte.pdf");
            }
        } catch (FileNotFoundException | JRException ignored) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al generar el reporte");
        }
        return ResponseEntity.ok().body(new ResponseFormat(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), null));
    }

}
