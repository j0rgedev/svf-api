package com.integrador.svfapi.classes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "student_representatives")
public class StudentRepresentatives {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_cod", referencedColumnName = "student_cod")
    private Student student_cod;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "representative_dni", referencedColumnName = "dni")
	private Representatives representativesDni;
    @Column(
            name = "relation",
            nullable = false,
            columnDefinition = "TEXT"
    )
	private String relation;
}
