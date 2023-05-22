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
    private Student student;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "representatives_dni", referencedColumnName = "dni")
	private Representative representative;
    @Column(
            name = "relation",
            nullable = false,
            columnDefinition = "TEXT"
    )
	private String relation;
    @Id
    @Column(
            name = "relation_code",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String relationCode;
}
