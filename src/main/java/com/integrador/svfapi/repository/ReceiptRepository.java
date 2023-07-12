package com.integrador.svfapi.repository;

import com.integrador.svfapi.classes.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, String> {
    Receipt findTopByOrderByReceiptCodDesc();
}
