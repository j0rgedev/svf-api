package com.integrador.svfapi;

import com.integrador.svfapi.classes.Student;
import com.integrador.svfapi.utils.AESEncryption;
import com.integrador.svfapi.utils.PasswordEncryption;

import java.time.LocalDate;

public class TestClass {

    public static void main(String[] args) {
        PasswordEncryption passwordEncryption = new PasswordEncryption();
        String pwd = "admin123";
        String salt = passwordEncryption.getSaltvalue(30);
        String encryptedPwd = passwordEncryption.generateSecurePassword(pwd, salt);
        //System.out.println("Salt: " + salt);
        //System.out.println("Encrypted password: " + encryptedPwd);
        System.out.println(passwordEncryption.verifyUserPassword("admin123",
                "4qncXUgulPqp+TqEyWKX+4WdRD5fOWBsLmOG+2MeMSU=",
                "gDbUomT2gdIZKBCZaTwXn6Gq8ws8KF"));

    }
}
