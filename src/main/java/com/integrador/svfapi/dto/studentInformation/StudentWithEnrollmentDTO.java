package com.integrador.svfapi.dto.studentInformation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentWithEnrollmentDTO {

    private String studentCod;
    private String names;
    private String lastNames;
    private String enrollmentID;
}
