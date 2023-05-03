package com.integrador.svfapi.classes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "level_costs")
public class LevelCosts {

    @Id
    @Column(
            name = "level_id"
    )
    private String levelId;
    @Column(
            name = "name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String name;
    @Column(
            name = "cost",
            nullable = false,
            columnDefinition = "DECIMAL(10,2)"
    )
    private BigDecimal cost;
    @ManyToOne
    @JoinColumn(name = "terms_conditions_id")
    private TermsAndConditions termsConditionsId;
}
