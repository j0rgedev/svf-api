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
@Entity(name = "users")
public class Users {

    @Id
    @Column(
            name = "user_id"
    )
    private String userId;
    @Column(
            name = "is_active",
            nullable = false,
            columnDefinition = "tinyint(1)"
    )
    private boolean isActive;
}
