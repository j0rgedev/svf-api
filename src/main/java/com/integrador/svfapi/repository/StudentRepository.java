package com.integrador.svfapi.repository;

import com.integrador.svfapi.dto.StudentReportData;
import com.integrador.svfapi.dto.dashboardDTO.EnrollmentCountByYearAndLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import com.integrador.svfapi.classes.Student;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
                    "WHERE u.is_active = true " +
                    "ORDER BY e.date DESC " +
                    "LIMIT 5", nativeQuery = true)
    List<Student> getLastFiveEnrolledStudents();

    @Query(value =  "SELECT s.* " +
            "FROM student s " +
            "INNER JOIN user u ON s.user_id = u.user_id " +
            "INNER JOIN enrollment e ON s.student_cod = e.student_cod " +
            "WHERE u.is_active = true and month(e.date) = :monthNumber " +
            "ORDER BY e.date DESC " +
            "LIMIT 5", nativeQuery = true)
    List<Student> getLastFiveEnrolledStudentsByMonth(@Param("monthNumber") int monthNumber);

    @Query(value =  "SELECT YEAR(e.date) AS year, s.current_level AS currentLevel, COUNT(*) AS count " +
                    "FROM student s " +
                    "INNER JOIN enrollment e ON s.student_cod = e.student_cod " +
                    "WHERE s.is_enrolled = true " +
                    "GROUP BY YEAR(e.date), s.current_level ", nativeQuery = true)
    List<EnrollmentCountByYearAndLevel> getEnrollmentCountByYearAndLevel();

    List<Student> findByIsEnrolled(boolean isEnrolled);

    @Query(value =  "SELECT " +
                    "    p.student_cod AS studentCod, " +
                    "    CONCAT(s.names, ' ', s.last_names) AS studentName, " +
                    "    SUM(IF(DATE_FORMAT(p.due_date, '%m') = '03' and p.status is false, p.amount, 0)) AS cuotaMarzo, " +
                    "    SUM(IF(DATE_FORMAT(p.due_date, '%m') = '04' and p.status is false, p.amount, 0)) AS cuotaAbril, " +
                    "    SUM(IF(DATE_FORMAT(p.due_date, '%m') = '05' and p.status is false, p.amount, 0)) AS cuotaMayo, " +
                    "    SUM(IF(DATE_FORMAT(p.due_date, '%m') = '06' and p.status is false, p.amount, 0)) AS cuotaJunio, " +
                    "    SUM(IF(DATE_FORMAT(p.due_date, '%m') = '07' and p.status is false, p.amount, 0)) AS cuotaJulio, " +
                    "    SUM(IF(DATE_FORMAT(p.due_date, '%m') = '08' and p.status is false, p.amount, 0)) AS cuotaAgosto, " +
                    "    SUM(IF(DATE_FORMAT(p.due_date, '%m') = '09' and p.status is false, p.amount, 0)) AS cuotaSeptiembre, " +
                    "    SUM(IF(DATE_FORMAT(p.due_date, '%m') = '10' and p.status is false, p.amount, 0)) AS cuotaOctubre, " +
                    "    SUM(IF(DATE_FORMAT(p.due_date, '%m') = '11' and p.status is false, p.amount, 0)) AS cuotaNoviembre, " +
                    "    SUM(IF(DATE_FORMAT(p.due_date, '%m') = '12' and p.status is false, p.amount, 0)) AS cuotaDiciembre, " +
                    "    SUM(IF(p.status = false, p.amount, 0)) AS totalPendiente " +
                    "FROM student s inner join pension p on p.student_cod = s.student_cod " +
                    "GROUP BY p.student_cod ", nativeQuery = true)
    List<StudentReportData> getStudentsReportData();
}
