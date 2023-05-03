package com.integrador.svfapi.repository;

import com.integrador.svfapi.classes.TermsAndConditions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TermsAndConditionsRepository extends JpaRepository<TermsAndConditions, String> {

}
