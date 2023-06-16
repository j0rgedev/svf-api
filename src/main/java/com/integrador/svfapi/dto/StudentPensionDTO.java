package com.integrador.svfapi.dto;

import java.time.LocalDate;
import java.util.List;

public record StudentPensionDTO(
        double totalDebt,
        List<Object> pensions
) {
}
