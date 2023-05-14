package com.integrador.svfapi.service.impl;

import com.integrador.svfapi.classes.ResponseFormat;
import com.integrador.svfapi.classes.Sms;
import com.integrador.svfapi.classes.Student;
import com.integrador.svfapi.dto.AuthDTO;
import com.integrador.svfapi.exception.BusinessException;
import com.integrador.svfapi.repository.SmsRepository;
import com.integrador.svfapi.repository.StudentRepository;
import com.integrador.svfapi.service.AuthService;
import com.integrador.svfapi.utils.AESEncryption;
import com.integrador.svfapi.utils.JwtUtil;
import com.integrador.svfapi.utils.PasswordEncryption;
import com.integrador.svfapi.utils.TwilioSMS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Random;

@Service
public class AuthServiceIMPL implements AuthService {

    private final JwtUtil jwtUtil;
    private final TwilioSMS twilioSMS;
    private final StudentRepository studentRepository;
    private final SmsRepository smsRepository;
    private final PasswordEncryption passwordEncryption;
    private final AESEncryption aesEncryption;

    @Autowired
    public AuthServiceIMPL(
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

    @Override
    public ResponseEntity<ResponseFormat> login(AuthDTO authDTO) {
        String studentCode = authDTO.getStudentCod();
        Student student = studentRepository.getReferenceById(studentCode);
        boolean isDefaultPassword = checkPasswordDefaultFormat(studentCode);

        if (checkCredentials(authDTO)) {
            if (isDefaultPassword) {
                String token = jwtUtil.generateToken(studentCode, 5 * 60 * 1000); // 5 minutes
                String smsCode = String.valueOf(generateRandomNumber());
                String redirectUrl = "/matricula/validacion-sms/?tempToken=" + token;
                saveSms(studentCode, smsCode);
                //Sms sending
                String studentPhoneNumber = student.getPhone();
                twilioSMS.sendMessage(studentPhoneNumber, smsCode);
                return ResponseEntity.ok().body(new ResponseFormat(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), redirectUrl));
            } else {
                String accessToken = jwtUtil.generateToken(studentCode, 24 * 60 * 60 * 1000); // 24 hours
                if (accessToken == null) {
                    throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "Error generating token");
                }
                return ResponseEntity.ok().body(new ResponseFormat(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), accessToken));
            }
        } else {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "Login failed");
        }
    }

    @Override
    public ResponseEntity<ResponseFormat> validateSMS(String accessToken, String sms) {
        String studentCod = jwtUtil.extractUsername(accessToken);
        if(jwtUtil.validateToken(accessToken, studentCod)) {
            String smsCode = getSmsCode(studentCod);
            if (smsCode.equals(sms)) {
                deleteSmsCode(studentCod);
                String tempToken = jwtUtil.generateToken(studentCod, 5 * 60 * 1000); // 5 minutes
                if (tempToken == null) {
                    throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "Error generating token");
                }
                return ResponseEntity.ok().body(new ResponseFormat(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), tempToken));
            } else {
                throw new BusinessException(HttpStatus.UNAUTHORIZED, "Invalid sms code");
            }
        } else {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }
    }

    @Override
    public ResponseEntity<ResponseFormat> updatePassword(String token, String password) {
        String studentCod = jwtUtil.extractUsername(token);
        if (jwtUtil.validateToken(token, studentCod)) {
            Student student = studentRepository.getReferenceById(studentCod);
            String salt = student.getSalt();
            String hashedPassword = passwordEncryption.generateSecurePassword(password, salt);
            student.setPassword(hashedPassword);
            studentRepository.save(student);
            String newAccessToken = jwtUtil.generateToken(studentCod, 24 * 60 * 60 * 1000); // 24 hours
            if (newAccessToken == null) {
                throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "Error generating token");
            }
            return ResponseEntity.ok().body(new ResponseFormat(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), newAccessToken));
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
        } else {
            return true;
        }
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
