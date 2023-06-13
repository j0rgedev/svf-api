package com.integrador.svfapi.classes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Pension {
    @Id
    @Column(name = "pension_cod")
    private int pension_cod;
    @Column(name = "due_date")
    private LocalDate due_date;
    @Column(name = "amount")
    private double amount;
    @Column(name = "status")
    private boolean status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_cod")
    private Student student;
}
