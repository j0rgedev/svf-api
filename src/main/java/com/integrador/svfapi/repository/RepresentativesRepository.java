package com.integrador.svfapi.repository;

import com.integrador.svfapi.classes.Representatives;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepresentativesRepository extends JpaRepository<Representatives,String> {
}
