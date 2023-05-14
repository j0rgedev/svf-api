package com.integrador.svfapi.controllers;

import com.integrador.svfapi.dto.AuthDTO;
import com.integrador.svfapi.service.AuthServiceIMPL;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/enrollment")
public class AuthController {

    private final AuthServiceIMPL authServiceIMPL;
    @Autowired
    public AuthController(AuthServiceIMPL authServiceIMPL) {
        this.authServiceIMPL = authServiceIMPL;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Validated @RequestBody AuthDTO authDTO
    ) {
        return authServiceIMPL.login(authDTO);
    }

    @PostMapping("/smsvalidation")
    public ResponseEntity<?> validateSms(
            @RequestHeader("Authorization") @NotBlank String tempToken,
            @NotBlank @NotNull @RequestBody() Map<String,String> sms
    ){
        if(!tempToken.startsWith("Bearer ")) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        tempToken = tempToken.replace("Bearer ", "");
        return authServiceIMPL.validateSMS(tempToken, sms.get("sms"));
    }

    @PutMapping("/updatepassword")
    public ResponseEntity<?> updatePassword(
            @RequestHeader("Authorization") @NotBlank String token,
            @NotBlank @NotNull @RequestBody() Map<String,String> password
    ) {
        if(!token.startsWith("Bearer ")) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        token = token.replace("Bearer ", "");
        return authServiceIMPL.updatePassword(token, password.get("password"));
    }
    
}
