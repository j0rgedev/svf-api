package com.integrador.svfapi.service.impl;

import com.integrador.svfapi.classes.Pension;
import com.integrador.svfapi.classes.ResponseFormat;
import com.integrador.svfapi.dto.ReportDTO;
import com.integrador.svfapi.dto.StudentReportData;
import com.integrador.svfapi.exception.BusinessException;
import com.integrador.svfapi.repository.PensionRepository;
import com.integrador.svfapi.repository.StudentRepository;
import com.integrador.svfapi.service.ReportService;
import com.integrador.svfapi.utils.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.sql.DataSource;
import java.io.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    private final StudentRepository studentRepository;
    private final JasperReportManager reportManager;
    private final JwtUtil jwtUtil;
    private final PensionRepository pensionRepository;

    @Autowired
    public ReportServiceImpl(StudentRepository studentRepository, JasperReportManager reportManager, JwtUtil jwtUtil, PensionRepository pensionRepository) {
        this.studentRepository = studentRepository;
        this.reportManager = reportManager;
        this.jwtUtil = jwtUtil;
        this.pensionRepository = pensionRepository;
    }

    @Override
    public ResponseEntity<ResponseFormat> exportReport(String token, String reportFormat) {
        TokenValidationResult tokenValidationResult = jwtUtil.validateToken(token);
        if (tokenValidationResult.isValid() && tokenValidationResult.tokenType().equals(TokenType.ADMIN)) {
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
        } else {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "No autorizado");
        }
    }

    @Override
    public ResponseEntity<Resource> mainReport(String token, Map<String, Object> params) {
        TokenValidationResult tokenValidationResult = jwtUtil.validateToken(token);
        if (tokenValidationResult.isValid() && tokenValidationResult.tokenType().equals(TokenType.ADMIN)) {
            String fileName = "reporte_general";
            ReportDTO dto = new ReportDTO();
            String extension = params.get("tipo").toString().equalsIgnoreCase(ReportType.EXCEL.name()) ? ".xlsx" : ".pdf";
            dto.setFileName(fileName + extension);
            try {
                DataSource dataSource = null;
                ByteArrayOutputStream stream = reportManager.export(fileName, params.get("tipo").toString(), params, dataSource.getConnection());
                byte[] bs = stream.toByteArray();
                dto.setStream(new ByteArrayInputStream(bs));
                dto.setLength(bs.length);
                InputStreamResource streamResource = new InputStreamResource(dto.getStream());
                MediaType mediaType = null;
                if (params.get("tipo").toString().equalsIgnoreCase(ReportType.EXCEL.name())) {
                    mediaType = MediaType.APPLICATION_OCTET_STREAM;
                } else {
                    mediaType = MediaType.APPLICATION_PDF;
                }
                return ResponseEntity.ok()
                        .header("Content-Disposition", "inline; filename=\"" + dto.getFileName() + "\"")
                        .contentLength(dto.getLength()).contentType(mediaType).body(streamResource);
            } catch (IOException | JRException | SQLException e) {
                throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al generar el reporte");
            }
        } else {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "No autorizado");
        }
    }

    @Override
    public ResponseEntity<InputStreamResource> pensionsReport(String token) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Pensiones");
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Código");
        headerRow.createCell(1).setCellValue("Fecha de pago");
        headerRow.createCell(2).setCellValue("Estado");
        headerRow.createCell(3).setCellValue("Monto");

        List<Pension> pensions = pensionRepository.findAll();
        int rowNum = 1;
        for (Pension pension : pensions) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(pension.getPensionCod());
            row.createCell(1).setCellValue(pension.getDueDate().toString());
            row.createCell(2).setCellValue(pension.isStatus() ? "Pagado" : "Pendiente");
            row.createCell(3).setCellValue(pension.getAmount());
        }

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();

            byte[] bytes = outputStream.toByteArray();
            outputStream.close();

            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            InputStreamResource resource = new InputStreamResource(inputStream);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=pensiones.xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(resource);
        } catch (IOException e) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al generar el reporte");
        }
    }
}
