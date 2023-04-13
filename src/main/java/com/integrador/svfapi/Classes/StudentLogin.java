package com.integrador.svfapi.Classes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentLogin {
    @NotNull
    @NotBlank
    private String studentCod;
    @NotNull
    @NotBlank
    private String password;
}
