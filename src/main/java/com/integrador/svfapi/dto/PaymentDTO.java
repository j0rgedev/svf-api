package com.integrador.svfapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentDTO {
    @NotNull
    @NotBlank
    private String paymentId;
    @NotNull
    @NotBlank
    private String paymentType;
}
