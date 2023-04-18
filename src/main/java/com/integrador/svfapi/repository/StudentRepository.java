package com.integrador.svfapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.integrador.svfapi.classes.Student;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {


}
