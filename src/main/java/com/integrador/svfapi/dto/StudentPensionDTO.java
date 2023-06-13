package com.integrador.svfapi.dto;

import java.time.LocalDate;

public record StudentPensionDTO(
        int pensionCode,
        String pensionName,
        double pensionAmount,
        LocalDate pensionDueDate,
        String pensionStatus
) {
}
