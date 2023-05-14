package com.integrador.svfapi.service.interfaces;

import com.integrador.svfapi.classes.ResponseFormat;
import com.integrador.svfapi.dto.AuthDTO;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<ResponseFormat> login (AuthDTO authDTO);
    ResponseEntity<ResponseFormat> validateSMS (String accessToken, String sms);
    ResponseEntity<ResponseFormat> updatePassword (String token, String password);
}
