package com.integrador.svfapi.dto.studentInformation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentWithOutEnrollmentDTO {

    @NotNull
    @NotBlank
    private String studentCod;
    @NotNull
    @NotBlank
    private String names;
    @NotNull
    @NotBlank
    private String lastnames;
    @NotNull
    @NotBlank
    private String dni;
    @NotNull
    @NotBlank
    private String newLevel;
    @NotNull
    @NotBlank
    private char newGrade;
}
