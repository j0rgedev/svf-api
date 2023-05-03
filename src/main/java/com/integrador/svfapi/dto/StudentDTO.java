package com.integrador.svfapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentDTO {

    @NotNull
    @NotBlank
    private String student_cod;
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
    private String currentLevel;
    @NotNull
    @NotBlank
    private char currentGrade;
}
