package com.integrador.svfapi.classes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "receipt")
public class Receipt {
    @Id
    @Column(
            name = "receipt_cod",
            columnDefinition = "char(7)"
    )
    private String receiptCod;
    @Column(
            name = "issuance_date",
            nullable = false,
            columnDefinition = "datetime"
    )
    private Timestamp issuanceDate;
    @Column(
            name = "total_amount",
            nullable = false,
            columnDefinition = "decimal(5,2)"
    )
    private long totalAmount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", referencedColumnName = "payment_id")
    private Payments payments;
    @Column(
            name = "payment_date",
            nullable = false,
            columnDefinition = "date"
    )
    private LocalDate paymentDate;
    @Column(
            name = "additional_information",
            nullable = false,
            columnDefinition = "varchar(100)"
    )
    private String additionalInformation;
}
