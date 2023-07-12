package com.integrador.svfapi.repository;

import com.integrador.svfapi.classes.Payments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentsRepository extends JpaRepository<Payments, String> {
}
