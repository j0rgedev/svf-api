package com.integrador.svfapi.utils;

public class CodeValidator {
    private static final String ADMIN_PREFIX = "SVFA";
    private static final String STUDENT_PREFIX = "SVF";

    public static boolean isAdminCode(String code) {
        return code.startsWith(ADMIN_PREFIX) && code.length() == ADMIN_PREFIX.length() + 3;
    }

    public static boolean isStudentCode(String code) {
        return code.startsWith(STUDENT_PREFIX) && code.length() == STUDENT_PREFIX.length() + 4;
    }
}
