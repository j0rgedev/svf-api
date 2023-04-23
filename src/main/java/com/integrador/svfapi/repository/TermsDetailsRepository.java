package com.integrador.svfapi.repository;

import com.integrador.svfapi.classes.TermsAndConditions;
import com.integrador.svfapi.classes.TermsDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TermsDetailsRepository extends JpaRepository<TermsDetails,String> {

    List<TermsDetails> findByTermsAndConditions(TermsAndConditions termsAndConditions);
}
