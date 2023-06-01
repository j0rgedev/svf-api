package com.integrador.svfapi.service.impl;

import com.integrador.svfapi.classes.Admin;
import com.integrador.svfapi.classes.ResponseFormat;
import com.integrador.svfapi.classes.Sms;
import com.integrador.svfapi.classes.Student;
import com.integrador.svfapi.dto.AuthDTO;
import com.integrador.svfapi.exception.BusinessException;
import com.integrador.svfapi.repository.AdminRepository;
import com.integrador.svfapi.repository.SmsRepository;
import com.integrador.svfapi.repository.StudentRepository;
import com.integrador.svfapi.service.AuthService;
import com.integrador.svfapi.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Random;

@Service
public class AuthServiceImpl implements AuthService {

    private final JwtUtil jwtUtil;
    private final TwilioSMS twilioSMS;
    private final StudentRepository studentRepository;
    private final AdminRepository adminRepository;
    private final SmsRepository smsRepository;
    private final PasswordEncryption passwordEncryption;
    private final AESEncryption aesEncryption;

    @Autowired
    public AuthServiceImpl(
            JwtUtil jwtUtil,
            TwilioSMS twilioSMS,
            StudentRepository studentRepository,
            AdminRepository adminRepository, SmsRepository smsRepository,
            PasswordEncryption passwordEncryption,
            AESEncryption aesEncryption
    ) {
        this.jwtUtil = jwtUtil;
        this.twilioSMS = twilioSMS;
        this.studentRepository = studentRepository;
        this.adminRepository = adminRepository;
        this.smsRepository = smsRepository;
        this.passwordEncryption = passwordEncryption;
        this.aesEncryption = aesEncryption;
    }

    @Override
    public ResponseEntity<ResponseFormat> login(AuthDTO authDTO) {
        String userCode = authDTO.getUserCode();
        if (CodeValidator.isStudentCode(userCode) && checkCredentials(authDTO)) {
            Student student = studentRepository.getReferenceById(userCode);
            String studentCod = student.getStudentCod();
            boolean isDefaultPassword = checkPasswordDefaultFormat(studentCod);
            if (isDefaultPassword) {
                String token = jwtUtil.generateToken(studentCod, 5 * 60 * 1000); // 5 minutes
                String smsCode = String.valueOf(generateRandomNumber());
                String redirectUrl = "/matricula/validacion/?tempToken=" + token;
                saveSms(studentCod, smsCode);
                //Sms sending
                String studentPhoneNumber = student.getPhone();
                twilioSMS.sendMessage(studentPhoneNumber, smsCode);
                String msg = "El usuario posee una contraseña con el formato default";
                HashMap<String, String> data = new HashMap<>();
                data.put("redirectUrl", redirectUrl);
                return ResponseEntity.ok().body(new ResponseFormat(HttpStatus.OK.value(), msg, data));
            } else {
                String accessToken = jwtUtil.generateToken(studentCod, 24 * 60 * 60 * 1000); // 24 hours
                if (accessToken == null) {
                    throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "Error generating token");
                }
                HashMap<String, String> data = new HashMap<>();
                data.put("accessToken", accessToken);
                return ResponseEntity.ok().body(new ResponseFormat(
                        HttpStatus.OK.value(),
                        HttpStatus.OK.getReasonPhrase(),
                        data));
            }
        } else if (CodeValidator.isAdminCode(userCode) && checkCredentials(authDTO)) {
            String accessToken = jwtUtil.generateToken(authDTO.getUserCode(), 24 * 60 * 60 * 1000); // 24 hours
            if (accessToken == null) {
                throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "Error generating token");
            }
            HashMap<String, String> data = new HashMap<>();
            data.put("accessToken", accessToken);
            return ResponseEntity.ok().body(new ResponseFormat(
                    HttpStatus.OK.value(),
                    HttpStatus.OK.getReasonPhrase(),
                    data));
        } else {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "Login failed");
        }
    }

    @Override
    public ResponseEntity<ResponseFormat> validateSMS(String accessToken, String sms) {
        TokenValidationResult tokenValidationResult = jwtUtil.validateToken(accessToken);
        if(tokenValidationResult.isValid() && tokenValidationResult.tokenType().equals(TokenType.STUDENT)) {
            String studentCod =tokenValidationResult.code();
            String smsCode = getSmsCode(studentCod);
            if (smsCode.equals(sms)) {
                deleteSmsCode(studentCod);
                String tempToken = jwtUtil.generateToken(studentCod, 5 * 60 * 1000); // 5 minutes
                if (tempToken == null) {
                    throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "Error generating token");
                }
                String msg = "La validación por SMS se realizado correctamente";
                HashMap<String, String> data = new HashMap<>();
                data.put("tempToken", tempToken);
                return ResponseEntity.ok().body(new ResponseFormat(HttpStatus.OK.value(), msg, data));
            } else {
                throw new BusinessException(HttpStatus.UNAUTHORIZED, "Invalid sms code");
            }
        } else {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }
    }

    /**
     * Este método se encarga de actualizar la contraseña de los usuarios que poseen una contraseña con el formato default.
     * @param token Token de autenticación
     * @param password Nueva contraseña
     * @return ResponseEntity con un objeto personalizado para la respuesta de tipo ResponseFormat.
     */
    @Override
    public ResponseEntity<ResponseFormat> updatePassword(String token, String password) {
        TokenValidationResult tokenValidationResult = jwtUtil.validateToken(token);
        if (tokenValidationResult.isValid() && tokenValidationResult.tokenType().equals(TokenType.STUDENT)) {
            String studentCod = tokenValidationResult.code();
            Student student = studentRepository.getReferenceById(studentCod);
            String salt = student.getSalt();
            String hashedPassword = passwordEncryption.generateSecurePassword(password, salt);
            student.setPassword(hashedPassword);
            studentRepository.save(student);
            String newAccessToken = jwtUtil.generateToken(studentCod, 24 * 60 * 60 * 1000); // 24 hours
            if (newAccessToken == null) {
                throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "Error generating token");
            }
            String msg = "La contraseña se ha actualizado correctamente";
            HashMap<String, String> data = new HashMap<>();
            data.put("accessToken", newAccessToken);
            return ResponseEntity.ok().body(new ResponseFormat(HttpStatus.OK.value(), msg, data));
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
        String providedPassword = authDTO.getPassword();
        String userPassword = "";
        String userSalt = "" ;
        if (CodeValidator.isStudentCode(authDTO.getUserCode())) {
            Student student= studentRepository.findById(authDTO.getUserCode())
                    .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Estudiante no econtrado"));
            userPassword = student.getPassword();
            userSalt = student.getSalt();
        }

        if (CodeValidator.isAdminCode(authDTO.getUserCode())) {
            Admin admin = adminRepository.findById(authDTO.getUserCode())
                    .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Administrador no encontrado"));
            userPassword = admin.getPassword();
            userSalt = admin.getSalt();
        }

        if (!passwordEncryption.verifyUserPassword(providedPassword, userPassword, userSalt)) {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "Credenciales Inválidas" );
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
