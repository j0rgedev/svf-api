package com.integrador.svfapi.dto.updateStudentBody;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
public class UpdateStudentInfoDTO {
    @NotNull
    private LocalDate newBirthday;
    @NotBlank
    @NotNull
    private String newDni;
    @NotBlank
    @NotNull
    private String newAddress;
    @NotBlank
    @NotNull
    private String newEmail;
    @NotBlank
    @NotNull
    private String newPhone;
    @NotNull
    private char newGrade;
    @NotBlank
    @NotNull
    private String newLevel;
}
