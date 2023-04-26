package com.integrador.svfapi.dto.enrollmentProcessBody;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LevelDetailsDTO {

    @NotNull
    @NotBlank
    private String levelId;
    @NotNull
    @NotBlank
    private String levelName;
}
