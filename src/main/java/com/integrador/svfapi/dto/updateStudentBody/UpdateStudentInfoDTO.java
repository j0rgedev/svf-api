package com.integrador.svfapi.dto.updateStudentBody;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class UpdateStudentInfoDTO {

    private String newNames;
    private String newLastName;
    private Date newBirthday;
    private String newDni;
    private String newAddress;
    private String newEmail;
    private String newPhone;
    private char newGrade;
    private String newLevel;
}
