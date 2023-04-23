package com.integrador.svfapi.dto.enrollmentDetailsResponse;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class LevelCostsDTO {

    private String name;
    private BigDecimal cost;
}
