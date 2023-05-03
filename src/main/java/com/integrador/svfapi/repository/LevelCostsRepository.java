package com.integrador.svfapi.repository;

import com.integrador.svfapi.classes.LevelCosts;
import com.integrador.svfapi.classes.TermsAndConditions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LevelCostsRepository extends JpaRepository<LevelCosts, String> {
    List<LevelCosts> findByTermsConditionsId(TermsAndConditions termsConditionsId);

}
