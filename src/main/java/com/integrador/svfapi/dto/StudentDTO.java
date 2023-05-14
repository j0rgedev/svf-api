package com.integrador.svfapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentDTO {
    private String student_cod;
    private String names;
    private String lastnames;
    private String dni;
    private String currentLevel;
    private char currentGrade;
}
