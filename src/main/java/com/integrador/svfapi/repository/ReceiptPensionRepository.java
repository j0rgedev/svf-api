package com.integrador.svfapi.repository;

import com.integrador.svfapi.classes.Pension;
import com.integrador.svfapi.classes.ReceiptPension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptPensionRepository extends JpaRepository<ReceiptPension, Integer> {
    ReceiptPension findByPension(Pension pension);
}
