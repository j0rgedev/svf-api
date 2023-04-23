package com.integrador.svfapi.dto;

import com.integrador.svfapi.classes.Payments;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EnrollmentDTO {

    @NotNull
    @NotBlank
    String studentCod;
    @NotNull
    @NotBlank
    String totalAmount;
    @NotNull
    @NotBlank
    String date;
    @NotNull
    @NotBlank
    LevelDetailsDTO levelDetailsDTO;
    @NotNull
    @NotBlank
    Payments payments;

}
