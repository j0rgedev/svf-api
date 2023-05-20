package com.integrador.svfapi.dto.addStudentBody;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddStudentBodyDTO {
    private StudentInfoDTO studentInfo;
    private RepresentativeInfoDTO representativeInfo;
}
