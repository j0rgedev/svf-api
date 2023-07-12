package com.integrador.svfapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.sql.Timestamp;
import java.util.List;

public record PensionsPayment(
        @NotNull List<Integer> pensionCod,
        @NotNull Timestamp paymentDate,
        @NotNull String paymentId) {
}


