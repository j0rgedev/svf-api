package com.integrador.svfapi;

import com.integrador.svfapi.classes.Student;
import com.integrador.svfapi.utils.AESEncryption;
import com.integrador.svfapi.utils.PasswordEncryption;

import java.time.LocalDate;

public class TestClass {

    public static void main(String[] args) {
    }



    private static boolean checkPasswordDefaultFormat(String names, String dni, String password, String salt) {
        PasswordEncryption passwordEncryption = new PasswordEncryption();
        int currentYear = LocalDate.now().getYear();
        String firstName = names.split(" ")[0].toLowerCase();
        String defaultPassword = createDefaultPasswordFormat(dni, firstName, currentYear);
        String hashedDefaultPassword = passwordEncryption.generateSecurePassword(defaultPassword, salt);
        return hashedDefaultPassword.equals(password);
    }

    private static String createDefaultPasswordFormat(String dni, String firstName, int currentYear) {
        return String.format("%d%s%s", currentYear, firstName, dni);
    }
}
