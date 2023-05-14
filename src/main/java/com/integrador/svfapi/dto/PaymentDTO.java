package com.integrador.svfapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentDTO {
    @NotNull
    @NotBlank
    private String paymentId;
    @NotNull
    @NotBlank
    private String paymentType;
}
