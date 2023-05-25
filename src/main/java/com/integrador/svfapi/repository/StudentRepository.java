package com.integrador.svfapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.integrador.svfapi.classes.Student;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
    Student findByStudentCod(String studentCod);
    Student findTopByOrderByStudentCodDesc();
    Student findStudentByLastNames(String studentLastNames);
    @Query(value = "select * from student s inner join user u on s.user_id=u.user_id where u.is_active is not false", nativeQuery = true)
    List<Student> findActiveStudents();
}
