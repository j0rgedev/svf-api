package com.integrador.svfapi.dto.addStudentBody;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class StudentInfoDTO {
    private String names;
    private String lastNames;
    private String dni;
    private Date birthday;
    private String address;
    private String email;
    private String phoneNumber;
    private String currentLevel;
    private char currentGrade;
}
