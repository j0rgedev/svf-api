package com.integrador.svfapi.repository;

import com.integrador.svfapi.classes.StudentRepresentatives;
import com.integrador.svfapi.classes.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepresentativesRepository extends JpaRepository<StudentRepresentatives, String> {

    StudentRepresentatives findTopByOrderByRelationCodeDesc();
}
