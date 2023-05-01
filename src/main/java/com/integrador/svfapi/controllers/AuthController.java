package com.integrador.svfapi.controllers;

import com.integrador.svfapi.dto.AuthDTO;
import com.integrador.svfapi.service.AuthService;
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

    private final AuthService authService;
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Validated @RequestBody AuthDTO authDTO
    ) {
        return authService.login(authDTO);
    }

    @PostMapping("/smsvalidation")
    public ResponseEntity<?> validateSms(
            @RequestParam("tempToken") @NotBlank String tempToken,
            @RequestParam("studentCod") @NotBlank String studentCod,
            @NotBlank @NotNull @RequestBody() Map<String,String> sms
    ){
        return authService.validateSms(tempToken, studentCod, sms.get("sms"));
    }

    @PutMapping("/updatepassword")
    public ResponseEntity<?> updatePassword(
            @RequestHeader("Authorization") @NotBlank String token,
            @NotBlank @NotNull @RequestBody() AuthDTO authDTO
    ) {
        if(!token.startsWith("Bearer ")) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        token = token.replace("Bearer ", "");
        return authService.updatePassword(token, authDTO);
    }



}
