package com.integrador.svfapi.dto.dashboardDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

@AllArgsConstructor
@Data
public class MonthPensionsCount {
    private int monthNumber;
    private int count;
}
