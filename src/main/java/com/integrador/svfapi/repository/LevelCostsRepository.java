package com.integrador.svfapi.repository;

import com.integrador.svfapi.classes.LevelCosts;
import com.integrador.svfapi.classes.TermsAndConditions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LevelCostsRepository extends JpaRepository<LevelCosts, String> {
    List<LevelCosts> findByTermsConditionsId(TermsAndConditions termsConditionsId);

}
