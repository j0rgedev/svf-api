package com.integrador.svfapi.dto.addStudentBody;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class StudentInfoDTO {
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
    private Date birthdate;
    @NotNull
    @NotBlank
    private String direction;
    @NotNull
    @NotBlank
    private String email;
    @NotNull
    @NotBlank
    private String phone;
    @NotNull
    @NotBlank
    private String level;
    @NotNull
    @NotBlank
    private char grade;
}
