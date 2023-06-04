package com.integrador.svfapi.repository;

import com.integrador.svfapi.dto.dashboardDTO.EnrollmentCountByYearAndLevel;
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
    @Query(value = "select s.* from student s inner join user u on s.user_id=u.user_id where u.is_active is not false", nativeQuery = true)
    List<Student> findActiveStudents();

    @Query(value =  "SELECT s.* " +
                    "FROM student s " +
                    "INNER JOIN user u ON s.user_id = u.user_id " +
                    "INNER JOIN enrollment e ON s.student_cod = e.student_cod " +
                    "INNER JOIN enrollment_details ed ON e.enrollment_id = ed.enrollment_id " +
                    "WHERE u.is_active = true " +
                    "ORDER BY ed.date DESC " +
                    "LIMIT 5", nativeQuery = true)
    List<Student> getLastFiveEnrolledStudents();

    @Query(value =  "SELECT YEAR(ed.date) AS year, s.current_level AS currentLevel, COUNT(*) AS count " +
                    "FROM student s " +
                    "INNER JOIN enrollment e ON s.student_cod = e.student_cod " +
                    "INNER JOIN enrollment_details ed ON e.enrollment_id = ed.enrollment_id " +
                    "WHERE s.is_enrolled = true " +
                    "GROUP BY YEAR(ed.date), s.current_level ", nativeQuery = true)
    List<EnrollmentCountByYearAndLevel> getEnrollmentCountByYearAndLevel();

    List<Student> findByIsEnrolled(boolean isEnrolled);
}
