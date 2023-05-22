package com.integrador.svfapi.service.impl;

import com.integrador.svfapi.classes.*;
import com.integrador.svfapi.dto.enrollmentDetailsResponse.EnrollmentDetailsDTO;
import com.integrador.svfapi.dto.enrollmentDetailsResponse.LevelCostsDTO;
import com.integrador.svfapi.dto.enrollmentDetailsResponse.TermDetailsDTO;
import com.integrador.svfapi.dto.enrollmentProcessBody.EnrollmentDTO;
import com.integrador.svfapi.repository.*;
import com.integrador.svfapi.service.EnrollmentService;
import com.integrador.svfapi.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    private final JwtUtil jwtUtil;
    private final EnrollmentRepository enrollmentRepository;
    private final EnrollmentDetailsRepository enrollmentDetailsRepository;
    private final TermsAndConditionsRepository termsAndConditionsRepository;
    private final TermsDetailsRepository termsDetailsRepository;
    private final LevelCostsRepository levelCostsRepository;

    @Autowired
    public EnrollmentServiceImpl(
            JwtUtil jwtUtil,
            EnrollmentRepository enrollmentRepository,
            EnrollmentDetailsRepository enrollmentDetailsRepository,
            TermsAndConditionsRepository termsAndConditionsRepository,
            TermsDetailsRepository termsDetailsRepository,
            LevelCostsRepository levelCostsRepository
    ) {
        this.jwtUtil = jwtUtil;
        this.enrollmentRepository = enrollmentRepository;
        this.enrollmentDetailsRepository = enrollmentDetailsRepository;
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
        String msg = "Se envian los datos de matricula del presente año escolar";

        return ResponseEntity.ok().body(new ResponseFormat(HttpStatus.OK.value(), msg, response));
    }

    @Override
    public ResponseEntity<ResponseFormat> enrollmentProcess(String token, EnrollmentDTO enrollmentDTO) {
        String studentCod = jwtUtil.extractUsername(token);
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
            enrollment.setTermsConditionsId(thisYearId);

            EnrollmentDetails enrollmentDetails = new EnrollmentDetails();
            enrollmentDetails.setEnrollmentId(newEnrollmentId);
            enrollmentDetails.setDate(enrollmentDTO.getDate());
            enrollmentDetails.setTotalAmount(enrollmentDTO.getTotalAmount());

            enrollmentRepository.saveAndFlush(enrollment);
            enrollmentDetailsRepository.saveAndFlush(enrollmentDetails);
        }
        String msg = "El Id de la nueva matrícula se ha generado correctamente";
        return ResponseEntity.ok().body(new ResponseFormat(HttpStatus.OK.value(), msg, newEnrollmentId));
    }

    private String createEnrollmentId(String id){
        DecimalFormat df = new DecimalFormat("E00000");
        int number = Integer.parseInt(id.substring(1,6));
        number++;

        return df.format(number);
    }

}
