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
@Entity(name = "terms_conditions")
public class TermsAndConditions {

    @Id
    @Column(
            name = "terms_conditions_id"
    )
    String termsConditionsId;
    @Column(
            name = "year",
            nullable = false,
            columnDefinition = "TEXT"
    )
    String year;
    @Column(
            name = "general_info",
            nullable = false,
            columnDefinition = "TEXT"
    )
    String generalInfo;

}
