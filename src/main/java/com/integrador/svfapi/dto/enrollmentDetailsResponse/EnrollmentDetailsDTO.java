package com.integrador.svfapi.dto.enrollmentDetailsResponse;

import com.integrador.svfapi.dto.enrollmentDetailsResponse.TermDetailsDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class EnrollmentDetailsDTO {

    private String schoolYear;
    private String mainInfo;
    private List<TermDetailsDTO> termDetails;
    private List<LevelCostsDTO> LevelCosts;
}
