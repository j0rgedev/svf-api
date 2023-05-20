package com.integrador.svfapi.classes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "representative")
public class Representative {

	@Id
	@Column(
			name = "dni"
	)
	private String dni;
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
	@Column(
			name = "occupation",
			nullable = false,
			columnDefinition = "TEXT"
	)
	private String occupation;
}
