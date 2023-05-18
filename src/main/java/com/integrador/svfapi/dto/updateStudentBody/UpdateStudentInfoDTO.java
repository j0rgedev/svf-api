package com.integrador.svfapi.dto.updateStudentBody;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class UpdateStudentInfoDTO {

    private String names;
    private String lastName;
    private Date birthday;
    private String dni;
    private String address;
    private String email;
    private String phone;
}
