package com.integrador.svfapi.repository;

import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.integrador.svfapi.classes.Student;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
    Student findByStudentCod(String studentCod);

    @Query(value = "SELECT MAX(s.student_cod) FROM Student s", nativeQuery = true)
    String findLastStudentCod();
    boolean existsById(@Nullable String studentCod);
}
