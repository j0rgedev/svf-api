package com.integrador.svfapi.repository;

import com.integrador.svfapi.classes.Enrollment;
import com.integrador.svfapi.classes.Student;
import com.integrador.svfapi.utils.EnrollmentCount;
import com.integrador.svfapi.utils.EnrollmentCountByMonth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment,String> {
    Enrollment findTopByOrderByEnrollmentIdDesc();

    Enrollment findByStudentCod(String studentCod);

    Enrollment findByStudentCodAndTermsConditionsId(String studentCod, String termsConditionsId);

    @Query(value = "SELECT month(date) AS month, " +
            "count(*) AS count " +
            "from enrollment " +
            "where year(date) = year(now()) " +
            "group by month(date)", nativeQuery = true)
    List<EnrollmentCount> getEnrollmentCount();


    @Query(value = "SELECT day(date) AS day, " +
            "count(*) AS count " +
            "FROM enrollment " +
            "WHERE year(date) = year(now()) AND month(date) = :monthNumber " +
            "GROUP BY day(date)", nativeQuery = true)
    List<EnrollmentCountByMonth> getEnrollmentCountByMonth(@Param("monthNumber") int monthNumber);
}
