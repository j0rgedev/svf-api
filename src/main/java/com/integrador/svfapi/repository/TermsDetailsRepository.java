package com.integrador.svfapi.repository;

import com.integrador.svfapi.classes.TermsAndConditions;
import com.integrador.svfapi.classes.TermsDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TermsDetailsRepository extends JpaRepository<TermsDetails,String> {
    List<TermsDetails> findByTermsConditionsId(TermsAndConditions termsConditionsId);

}
