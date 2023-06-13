package com.integrador.svfapi.dto.dashboardDTO;

public record StudentsPaymentStatus(
        int totalStudentsWithPayment,
        int totalStudentsWithoutPayment
) {
}
