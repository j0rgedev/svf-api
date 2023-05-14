package com.integrador.svfapi.dto.enrollmentProcessBody;

import com.integrador.svfapi.dto.PaymentDTO;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class EnrollmentDTO {
    @NotNull
    BigDecimal totalAmount;
    @NotNull
    Timestamp date;
    LevelDetailsDTO level;
    PaymentDTO paymentMethod;
}
