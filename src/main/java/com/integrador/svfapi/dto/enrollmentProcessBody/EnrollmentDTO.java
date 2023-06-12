package com.integrador.svfapi.dto.enrollmentProcessBody;

import com.integrador.svfapi.dto.PaymentDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class EnrollmentDTO {
    double totalAmount;
    LocalDate date;
    LevelDetailsDTO level;
    PaymentDTO paymentMethod;
}
