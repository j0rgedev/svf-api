package com.integrador.svfapi.repository;

import com.integrador.svfapi.classes.Sms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsRepository extends JpaRepository<Sms, String> {

}
