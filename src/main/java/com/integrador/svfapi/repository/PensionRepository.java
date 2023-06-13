package com.integrador.svfapi.repository;

import com.integrador.svfapi.classes.Pension;
import com.integrador.svfapi.classes.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PensionRepository extends JpaRepository<Pension, Integer> {
    List<Pension> findAllByStudent(Student student);
}
