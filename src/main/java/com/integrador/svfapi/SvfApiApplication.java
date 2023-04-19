package com.integrador.svfapi;

import com.integrador.svfapi.utils.PasswordEncryption;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SvfApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SvfApiApplication.class, args);
//        PasswordEncryption passwordEncryption = new PasswordEncryption();
//        String text = "2023jorge71209310";
//        String salt = passwordEncryption.getSaltvalue(30);
//        String password = passwordEncryption.generateSecurePassword(text, salt);
//        System.out.println("Password: " + password);
//        System.out.println("Salt: " + salt);
    }

}
