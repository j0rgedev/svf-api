package com.integrador.svfapi.classes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "admin")
public class Admin {

    @Id
    @Column(
            name = "admin_cod"
    )
    private String adminCod;
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
            name = "birthday",
            nullable = false,
            columnDefinition = "TEXT"
    )
	private String birthday;
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
	private String direction;
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
	private String number;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private Users user;
}
