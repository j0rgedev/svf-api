package com.integrador.svfapi.dto.getAllStudents;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class SingleStudentDTO {
    private String studentCod;
    private String names;
    private String lastNames;
    private Date birthday;
    private String dni;
    private String address;
    private String email;
    private String phone;
    private String currentLevel;
    private char currentGrade;
}
