package com.integrador.svfapi.classes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class EnrollmentDetails {

    @Id
    @Column(
            name = "enrollment_id"
    )
    private String enrollmentId;
    @Column(
            name = "date",
            nullable = false
    )
    private Timestamp date;
    @Column(
            name = "total_amount",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private BigDecimal totalAmount;

}
