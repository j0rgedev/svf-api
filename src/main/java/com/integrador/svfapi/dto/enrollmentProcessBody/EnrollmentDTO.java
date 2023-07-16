package com.integrador.svfapi.dto.enrollmentProcessBody;

import com.integrador.svfapi.dto.PaymentDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentDTO {
    double totalAmount;
    LocalDateTime date;
    LevelDetailsDTO level;
    PaymentDTO paymentMethod;
}
