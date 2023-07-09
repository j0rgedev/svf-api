package com.integrador.svfapi.repository;

import com.integrador.svfapi.classes.Pension;
import com.integrador.svfapi.classes.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PensionRepository extends JpaRepository<Pension, Integer> {
    List<Pension> findAllByStudent(Student student);

    @Query(value = "SELECT month(due_date) AS month, " +
            "if(status = 1, count(*), 0) AS count " +
            "FROM pension GROUP BY status , due_date", nativeQuery = true)
    List<Object[]> getPensionsQuantity();

    @Query(value = "SELECT month(due_date) AS month, " +
            "if(status = 1, count(*), 0) AS count " +
            "FROM pension where month(due_date) = :monthNumber " +
            "GROUP BY status , due_date limit 1", nativeQuery = true)
    List<Object[]> getPensionsQuantityByMonth(@Param("monthNumber") int monthNumber);

    @Query(value = "SELECT " +
            "SUM(p.amount) AS totalDebt, " +
            "SUM(IF(p.status = true, p.amount, 0)) AS totalPaid, " +
            "SUM(p.amount) - SUM(IF(p.status = true, p.amount, 0)) AS totalPending " +
            "FROM pension p " +
            "WHERE MONTH(p.due_date) = :monthNumber " +
            "GROUP BY MONTH(p.due_date)", nativeQuery = true)
    List<Double[]> getDebtByMonth(@Param("monthNumber") int monthNumber);
}
