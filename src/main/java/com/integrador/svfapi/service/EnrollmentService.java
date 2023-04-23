package com.integrador.svfapi.service;


import com.integrador.svfapi.classes.LevelCosts;
import com.integrador.svfapi.classes.TermsAndConditions;
import com.integrador.svfapi.classes.TermsDetails;
import com.integrador.svfapi.dto.enrollmentDetailsResponse.EnrollmentDetailsDTO;
import com.integrador.svfapi.dto.enrollmentDetailsResponse.LevelCostsDTO;
import com.integrador.svfapi.dto.enrollmentDetailsResponse.TermDetailsDTO;
import com.integrador.svfapi.repository.LevelCostsRepository;
import com.integrador.svfapi.repository.TermsAndConditionsRepository;
import com.integrador.svfapi.repository.TermsDetailsRepository;
import com.integrador.svfapi.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class EnrollmentService {

    private final JwtUtil jwtUtil;
    private final TermsAndConditionsRepository termsAndConditionsRepository;
    private final TermsDetailsRepository termsDetailsRepository;
    private final LevelCostsRepository levelCostsRepository;

    @Autowired
    public EnrollmentService(
            JwtUtil jwtUtil,
            TermsAndConditionsRepository termsAndConditionsRepository,
            TermsDetailsRepository termsDetailsRepository,
            LevelCostsRepository levelCostsRepository
    ) {
        this.jwtUtil = jwtUtil;
        this.termsAndConditionsRepository = termsAndConditionsRepository;
        this.termsDetailsRepository = termsDetailsRepository;
        this.levelCostsRepository = levelCostsRepository;
    }

    public ResponseEntity<EnrollmentDetailsDTO> enrollmentDetails (){

        LocalDate today = LocalDate.now();
        int year = today.getYear();
        String thisYearId = "T" + year;

        TermsAndConditions currentTermsAndConditions = termsAndConditionsRepository.getReferenceById(thisYearId);
        List<TermsDetails> currentTermDetails = termsDetailsRepository.findByTermsAndConditions(currentTermsAndConditions);
        List<LevelCosts> currentLevelCosts = levelCostsRepository.findByTermsAndConditions(currentTermsAndConditions);
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

        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<Map<String,String>> enrollmentProcess(String token){

        String enrollmentId= "";
        return ResponseEntity.ok().body(Map.of("enrollmentId", enrollmentId));
    }

}
