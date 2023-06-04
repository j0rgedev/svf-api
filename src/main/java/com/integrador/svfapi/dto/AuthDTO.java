package com.integrador.svfapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthDTO {
    @NotNull
    @NotBlank
    private String userCode;
    @NotNull
    @NotBlank
    private String password;
}
