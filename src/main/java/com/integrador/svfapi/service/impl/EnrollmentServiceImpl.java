package com.integrador.svfapi.service.impl;

import com.integrador.svfapi.classes.*;
import com.integrador.svfapi.dto.enrollmentDetailsResponse.EnrollmentDetailsDTO;
import com.integrador.svfapi.dto.enrollmentDetailsResponse.LevelCostsDTO;
import com.integrador.svfapi.dto.enrollmentDetailsResponse.TermDetailsDTO;
import com.integrador.svfapi.dto.enrollmentProcessBody.EnrollmentDTO;
import com.integrador.svfapi.exception.BusinessException;
import com.integrador.svfapi.repository.*;
import com.integrador.svfapi.service.EnrollmentService;
import com.integrador.svfapi.utils.JwtUtil;
import com.integrador.svfapi.utils.TokenType;
import com.integrador.svfapi.utils.TokenValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    private final JwtUtil jwtUtil;
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final PensionRepository pensionRepository;
    private final TermsAndConditionsRepository termsAndConditionsRepository;
    private final TermsDetailsRepository termsDetailsRepository;
    private final LevelCostsRepository levelCostsRepository;

    @Autowired
    public EnrollmentServiceImpl(
            JwtUtil jwtUtil, EnrollmentRepository enrollmentRepository,
            StudentRepository studentRepository, PensionRepository pensionRepository, TermsAndConditionsRepository termsAndConditionsRepository,
            TermsDetailsRepository termsDetailsRepository,
            LevelCostsRepository levelCostsRepository
    ) {
        this.jwtUtil = jwtUtil;
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.pensionRepository = pensionRepository;
        this.termsAndConditionsRepository = termsAndConditionsRepository;
        this.termsDetailsRepository = termsDetailsRepository;
        this.levelCostsRepository = levelCostsRepository;
    }

    @Override
    public ResponseEntity<ResponseFormat> enrollmentDetails() {
        LocalDate today = LocalDate.now();
        int year = today.getYear();
        String thisYearId = "T" + year;

        TermsAndConditions currentTermsAndConditions = termsAndConditionsRepository.getReferenceById(thisYearId);
        List<TermsDetails> currentTermDetails = termsDetailsRepository.findByTermsConditionsId(currentTermsAndConditions);
        List<LevelCosts> currentLevelCosts = levelCostsRepository.findByTermsConditionsId(currentTermsAndConditions);
        EnrollmentDetailsDTO response = new EnrollmentDetailsDTO();
        response.setSchoolYear(String.valueOf(year));
        response.setMainInfo(currentTermsAndConditions.getGeneralInfo());
        List<TermDetailsDTO> termsDTO = new ArrayList<>();
        for (TermsDetails termsDetails: currentTermDetails) {

            termsDTO.add(new TermDetailsDTO(termsDetails.getTermTitle(),termsDetails.getTermDescription()));
        }
        response.setTermDetails(termsDTO);
        List<LevelCostsDTO> amountsDTO = new ArrayList<>();
        for (LevelCosts levelCosts: currentLevelCosts) {

            amountsDTO.add(new LevelCostsDTO(levelCosts.getName(), levelCosts.getCost()));
        }
        response.setLevelCosts(amountsDTO);

        return ResponseEntity.ok().body(new ResponseFormat(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                response));
    }

    @Override
    public ResponseEntity<ResponseFormat> enrollmentProcess(String token, EnrollmentDTO enrollmentDTO) {
        TokenValidationResult tokenValidationResult = jwtUtil.validateToken(token);
        if (tokenValidationResult.isValid() && tokenValidationResult.tokenType().equals(TokenType.STUDENT)) {
            String studentCod = tokenValidationResult.code();
            String newEnrollmentId;
            LocalDate today = LocalDate.now();
            int year = today.getYear();
            String thisYearId = "T" + year;

            Enrollment foundEnrollment = enrollmentRepository.findByStudentCodAndTermsConditionsId(studentCod, thisYearId);
            if (foundEnrollment != null) {
                newEnrollmentId = foundEnrollment.getEnrollmentId();
            } else {
                String lastEnrollmentId = enrollmentRepository.findTopByOrderByEnrollmentIdDesc().getEnrollmentId();
                newEnrollmentId = createEnrollmentId(lastEnrollmentId);

                Enrollment enrollment = new Enrollment();
                enrollment.setEnrollmentId(newEnrollmentId);
                enrollment.setStudentCod(studentCod);
                enrollment.setPaymentId(enrollmentDTO.getPaymentMethod().getPaymentId());
                enrollment.setStatus(true);
                enrollment.setDate(enrollmentDTO.getDate());
                enrollment.setAmount(enrollmentDTO.getTotalAmount());
                enrollment.setTermsConditionsId(thisYearId);
                enrollmentRepository.saveAndFlush(enrollment);

                createStudentPensions(studentCod);
            }
            HashMap<String, String> data = new HashMap<>();
            data.put("enrollmentId", newEnrollmentId);
            return ResponseEntity.ok().body(new ResponseFormat(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), data));
        } else {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }
    }

    // Función que permite crear las pensiones de un estudiante
    private void createStudentPensions(String studentCod){
        LocalDate[] due_dates = {
                LocalDate.of(2023, 3, 6),
                LocalDate.of(2023, 4, 6),
                LocalDate.of(2023, 5, 6),
                LocalDate.of(2023, 6, 6),
                LocalDate.of(2023, 7, 6),
                LocalDate.of(2023, 8, 6),
                LocalDate.of(2023, 9, 6),
                LocalDate.of(2023, 10, 6),
                LocalDate.of(2023, 11, 6),
                LocalDate.of(2023, 12, 6)
        };
        Map<String, Double> pensionByLevel = new HashMap<>();
        pensionByLevel.put("Inicial", 300.00);
        pensionByLevel.put("Primaria", 350.00);
        pensionByLevel.put("Secundaria", 400.00);

        Student student = studentRepository.findById(studentCod)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "El estudiante no existe"));

        for (LocalDate dueDate : due_dates) {
            Pension studentPension = new Pension();
            studentPension.setDueDate(dueDate);
            studentPension.setAmount(pensionByLevel.get(student.getCurrentLevel()));
            studentPension.setStatus(false);
            studentPension.setStudent(student);
            pensionRepository.saveAndFlush(studentPension);
        }
    }

    private String createEnrollmentId(String id){
        DecimalFormat df = new DecimalFormat("E00000");
        int number = Integer.parseInt(id.substring(1,6));
        number++;

        return df.format(number);
    }

}
