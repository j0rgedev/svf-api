package com.integrador.svfapi.classes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "terms_details")
public class TermsDetails {

    @Id
    @Column(
            name = "term_id"
    )
    private String termId;
    @Column(
            name = "term_title",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String termTitle;
    @Column(
            name = "term_description",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String termDescription;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "terms_conditions_id")
    private TermsAndConditions termsConditionsId;
}
