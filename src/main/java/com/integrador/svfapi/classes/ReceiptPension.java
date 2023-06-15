package com.integrador.svfapi.classes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "receipt_pension")
public class ReceiptPension {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pension_cod", referencedColumnName = "pension_cod")
    private Pension pension;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receipt_cod", referencedColumnName = "receipt_cod")
    private Receipt receipt;
}
