package com.integrador.svfapi.repository;

import com.integrador.svfapi.classes.Pension;
import com.integrador.svfapi.classes.Student;
import com.integrador.svfapi.dto.dashboardDTO.DebtByMonth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PensionRepository extends JpaRepository<Pension, Integer> {
    List<Pension> findAllByStudent(Student student);

    @Query(value = "SELECT " +
            "SUM(p.amount) AS totalDebt, " +
            "SUM(IF(p.status = true, p.amount, 0)) AS totalPaid, " +
            "SUM(p.amount) - SUM(IF(p.status = true, p.amount, 0)) AS totalPending " +
            "FROM pension p " +
            "WHERE MONTH(p.due_date) = :monthNumber " +
            "GROUP BY MONTH(p.due_date)", nativeQuery = true)
    List<Double[]> getDebtByMonth(@Param("monthNumber") int monthNumber);
}
