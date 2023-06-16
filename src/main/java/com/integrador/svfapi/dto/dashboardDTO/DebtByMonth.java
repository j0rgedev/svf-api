package com.integrador.svfapi.dto.dashboardDTO;

public record DebtByMonth(
        double totalDebt,
        double totalPaid,
        double totalPending
) {
}
