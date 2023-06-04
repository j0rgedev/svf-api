package com.integrador.svfapi.dto.getAllStudents;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
public class StudentListDTO {

    @NotNull
    @NotBlank
    private String studentCod;
    @NotNull
    @NotBlank
    private String fullName;
    @NotNull
    @NotBlank
    private LocalDate birthday;
    @NotNull
    @NotBlank
    private boolean isEnrolled;
}
