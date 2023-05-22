package com.integrador.svfapi.classes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "student")
public class Student {

    @Id
    @Column(
            name = "student_cod",
            columnDefinition = "char(7)"
    )
    private String studentCod;
    @Column(
            name = "names",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String names;
    @Column(
            name = "last_names",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String lastNames;
    @Column(
            name = "birthday",
            nullable = false,
            columnDefinition = "DATE"
    )
    private Date birthday;
    @Column(
            name = "password",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String password;
    @Column(
            name = "salt",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String salt;
    @Column(
            name = "dni",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String dni;
    @Column(
            name = "direction",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String address;
    @Column(
            name = "email",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String email;
    @Column(
            name = "number",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String phone;
    @Column(
            name = "current_grade",
            nullable = false,
            columnDefinition = "char(1)"
    )
    private char currentGrade;
    @Column(
            name = "current_level",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String currentLevel;
    @Column(
            name = "is_enrolled",
            nullable = false,
            columnDefinition = "Tinyint(1)"
    )
    boolean isEnrolled;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

}
