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
public class Payments {

    @Id
    @Column(
            name = "payment_id"
    )
    private String paymentId;
    @Column(
            name = "payment_type",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String paymentType;
}
