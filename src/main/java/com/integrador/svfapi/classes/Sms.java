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
@Entity(name = "sms_validation")
public class Sms {
    @Id
    @Column(name = "student_cod")
    private String student_cod;
    @Column(name = "code")
    private String code;
    @Column(name = "expires_at")
    private Date expires_at;
    @Column(name = "created_at")
    private Date created_at;
}
