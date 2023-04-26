package com.integrador.svfapi.dto.enrollmentProcessBody;

import com.integrador.svfapi.classes.Payments;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class EnrollmentDTO {

    @NotNull
    @NotBlank
    String studentCod;
    @NotNull
    @NotBlank
    BigDecimal totalAmount;
    @NotNull
    @NotBlank
    Timestamp date;
    @NotNull
    @NotBlank
    LevelDetailsDTO levelDetailsDTO;
    @NotNull
    @NotBlank
    Payments payments;

}
