package com.integrador.svfapi.service;

import com.integrador.svfapi.classes.Sms;
import com.integrador.svfapi.dto.AuthDTO;
import com.integrador.svfapi.exception.BusinessException;
import com.integrador.svfapi.repository.SmsRepository;
import com.integrador.svfapi.repository.StudentRepository;
import com.integrador.svfapi.classes.Student;
import com.integrador.svfapi.utils.AESEncryption;
import com.integrador.svfapi.utils.PasswordEncryption;
import com.integrador.svfapi.utils.JwtUtil;
import com.integrador.svfapi.utils.TwilioSMS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

@Service
public class AuthService {

    private final JwtUtil jwtUtil;
    private final TwilioSMS twilioSMS;
    private final StudentRepository studentRepository;
    private final SmsRepository smsRepository;
    private final PasswordEncryption passwordEncryption;
    private final AESEncryption aesEncryption;

    @Autowired
    public AuthService(
            JwtUtil jwtUtil,
            TwilioSMS twilioSMS,
            StudentRepository studentRepository,
            SmsRepository smsRepository,
            PasswordEncryption passwordEncryption,
            AESEncryption aesEncryption
    ) {
        this.jwtUtil = jwtUtil;
        this.twilioSMS = twilioSMS;
        this.studentRepository = studentRepository;
        this.smsRepository = smsRepository;
        this.passwordEncryption = passwordEncryption;
        this.aesEncryption = aesEncryption;
    }

    //This method is to log in the user
    public ResponseEntity<Map<String, String>> login(AuthDTO authDTO) {
        String studentCode = authDTO.getStudentCod();
        boolean isDefaultPassword = checkPasswordDefaultFormat(studentCode);

        if (isDefaultPassword) {
            String token = jwtUtil.generateToken(studentCode, 5 * 60 * 1000); // 5 minutes
            String redirectUrl = "/matricula/validacion-sms/?tempToken=" + token;
//            String smsCode = String.valueOf(generateRandomNumber());
//            saveSms(studentCode, smsCode);
//            // Sms sending
//            String studentPhoneNumber = student.getPhone();
//            twilioSMS.sendMessage(studentPhoneNumber, smsCode);
            return ResponseEntity.ok().body(Map.of("redirectUrl", redirectUrl));
        } else if (checkCredentials(authDTO)) {
            String token = jwtUtil.generateToken(studentCode, 24 * 60 * 60 * 1000); // 24 hours
            if (token == null) {
                throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "Error generating token");
            }
            return ResponseEntity.ok().body(Map.of("accessToken", token));
        } else {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "Login failed");
        }
    }


    // This method is called when the user has already logged in and has a default password and
    // wants to change it
    public ResponseEntity<Map<String,String>> validateSms(String accessToken, String sms){
        String studentCod = jwtUtil.extractUsername(accessToken);
        if(jwtUtil.validateToken(accessToken, studentCod)) {
            String smsCode = getSmsCode(studentCod);
            if (smsCode.equals(sms)) {
                deleteSmsCode(studentCod);
                String token = jwtUtil.generateToken(studentCod, 5 * 60 * 1000); // 5 minutes
                if (token == null) {
                    throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "Error generating token");
                }
                return ResponseEntity.ok().body(Map.of("tempToken", token));
            } else {
                throw new BusinessException(HttpStatus.UNAUTHORIZED, "Invalid sms code");
            }
        } else {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }
    }

    // This method is to update the default password after sms validation
    public ResponseEntity<Map<String, String>> updatePassword(String token, AuthDTO authDTO) {
        String studentCod = authDTO.getStudentCod();
        if (jwtUtil.validateToken(token, studentCod)) {
            Student student = studentRepository.getReferenceById(studentCod);
            String salt = student.getSalt();
            String hashedPassword = passwordEncryption.generateSecurePassword(authDTO.getPassword(), salt);
            student.setPassword(hashedPassword);
            student.setSalt(salt);
            studentRepository.save(student);
            String newAccessToken = jwtUtil.generateToken(studentCod, 24 * 60 * 60 * 1000); // 24 hours
            if (newAccessToken == null) {
                throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "Error generating token");
            }
            return ResponseEntity.ok().body(Map.of("accessToken", newAccessToken));
        } else {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }
    }


    /*
        Functions for login process
    */
    private int generateRandomNumber() {
        return new Random().nextInt(90000)+10000; // 5 digits
    }

    private boolean checkCredentials(AuthDTO authDTO) {
        Student student = studentRepository.getReferenceById(authDTO.getStudentCod());
        String studentPassword = student.getPassword();
        String studentSalt = student.getSalt();
        String providedPassword = authDTO.getPassword();

        if (!passwordEncryption.verifyUserPassword(providedPassword, studentPassword, studentSalt)) {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "Login failed");
        }

        return true;
    }

    private boolean checkPasswordDefaultFormat(String studentCod) {
        Student student = studentRepository.getReferenceById(studentCod);
        int currentYear = LocalDate.now().getYear();
        String firstName = student.getNames().split(" ")[0];
        String defaultPassword = createDefaultPasswordFormat(student.getDni(), firstName, currentYear);
        String hashedDefaultPassword = passwordEncryption.generateSecurePassword(defaultPassword, student.getSalt());
        return hashedDefaultPassword.equals(student.getPassword());
    }

    private String createDefaultPasswordFormat(String dni, String firstName, int currentYear) {
        return String.format("%d%s%s", currentYear, firstName.toLowerCase(), dni);
    }

    /*
        Functions for sms validation process
    */
    public String getSmsCode(String studentCod){
        Sms sms = smsRepository.getReferenceById(studentCod);
        return aesEncryption.decrypt(sms.getCode());
    }

    private void saveSms(String studentCod, String code){
        Sms sms = new Sms();
        String encryptedSmsCode = aesEncryption.encrypt(code);
        Timestamp smsCreationDate = new Timestamp(System.currentTimeMillis());
        Timestamp smsExpirationDate = new Timestamp(smsCreationDate.getTime() + 5 * 60 * 1000); // 5 minutes
        sms.setStudent_cod(studentCod);
        sms.setCode(encryptedSmsCode);
        sms.setCreated_at(smsCreationDate);
        sms.setExpires_at(smsExpirationDate);
        smsRepository.save(sms);
    }

    public void deleteSmsCode(String studentCod){
        smsRepository.deleteById(studentCod);
    }


    }

