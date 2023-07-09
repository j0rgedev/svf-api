package com.integrador.svfapi.service.impl;

import com.integrador.svfapi.dto.ReportDTO;
import com.integrador.svfapi.service.ReportServiceAPI;
import com.integrador.svfapi.utils.JasperReportManager;
import com.integrador.svfapi.utils.ReportType;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

@Service
public class ReportServiceAPIImpl implements ReportServiceAPI {

    @Autowired
    private JasperReportManager reportManager;

    @Autowired
    private DataSource dataSource;
    @Override
    public ReportDTO getReport(Map<String, Object> params) {
        String fileName = "reporte_general";
        ReportDTO dto = new ReportDTO();
        String extension = params.get("tipo").toString().equalsIgnoreCase(ReportType.EXCEL.name()) ? ".xlsx" : ".pdf";
        dto.setFileName(fileName + extension);

        try {
            ByteArrayOutputStream stream = reportManager.export(fileName, params.get("tipo").toString(), params, dataSource.getConnection());
            byte[] bs = stream.toByteArray();
            dto.setStream(new ByteArrayInputStream(bs));
            dto.setLength(bs.length);
            return dto;
        } catch (IOException | JRException | SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
