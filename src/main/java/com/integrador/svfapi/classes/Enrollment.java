package com.integrador.svfapi.classes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Enrollment {

    @Id
    @Column(
            name = "enrollment_id"
    )
    String enrollmentId;
    @Column(
            name = "student_cod",
            nullable = false,
            columnDefinition = "TEXT"
    )
    String studentCod;
    @Column(
            name = "payment_id",
            nullable = false,
            columnDefinition = "TEXT"
    )
    String paymentId;
    @Column(
            name = "status",
            nullable = false,
            columnDefinition = "BOOLEAN"
    )
    boolean status;
    @Column(
            name = "terms_conditions_id",
            nullable = false,
            columnDefinition = "TEXT"
    )
    String termsConditionsId;
}
