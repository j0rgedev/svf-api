package com.integrador.svfapi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.integrador.svfapi.Classes.Student;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
}
