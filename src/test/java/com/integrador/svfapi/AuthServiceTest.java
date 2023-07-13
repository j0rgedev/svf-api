package com.integrador.svfapi;

import com.integrador.svfapi.classes.ResponseFormat;
import com.integrador.svfapi.classes.Student;
import com.integrador.svfapi.dto.AuthDTO;
import com.integrador.svfapi.repository.AdminRepository;
import com.integrador.svfapi.repository.SmsRepository;
import com.integrador.svfapi.repository.StudentRepository;
import com.integrador.svfapi.service.impl.AuthServiceImpl;
import com.integrador.svfapi.utils.AESEncryption;
import com.integrador.svfapi.utils.JwtUtil;
import com.integrador.svfapi.utils.PasswordEncryption;
import com.integrador.svfapi.utils.TwilioSMS;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthServiceTest {

    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private TwilioSMS twilioSMS;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private AdminRepository adminRepository;
    @Mock
    private SmsRepository smsRepository;
    @Mock
    private PasswordEncryption passwordEncryption;
    @Mock
    private AESEncryption aesEncryption;
    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    public void testLogin_WithValidStudentCode_ReturnsToken() {
        // Configuración del caso de prueba
        String studentCode = "SVF1234";
        AuthDTO authDTO = new AuthDTO(studentCode, "password");

        // Configuración del comportamiento del objeto mock studentRepository
        Student student = new Student();
        student.setStudentCod(studentCode);
        student.setNames("Names");
        student.setPassword("hashedPassword");
        student.setSalt("salt");

        Mockito.when(studentRepository.getReferenceById(studentCode)).thenReturn(student);
        Mockito.when(studentRepository.findById(studentCode)).thenReturn(Optional.of(student));
        Mockito.when(passwordEncryption.verifyUserPassword(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        Mockito.when(passwordEncryption.generateSecurePassword(Mockito.anyString(), Mockito.anyString())).thenReturn(student.getPassword());

        // Configuración del comportamiento del objeto mock jwtUtil
        String accessToken = "mockAccessToken";
        Mockito.when(jwtUtil.generateToken(studentCode, 24 * 60 * 60 * 1000)).thenReturn(accessToken);

        // Llamada al método a probar
        ResponseEntity<ResponseFormat> response = authService.login(authDTO);

        // Verificaciones
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        //Assertions.assertEquals(accessToken, response.getBody().data().("accessToken"));
    }

}
