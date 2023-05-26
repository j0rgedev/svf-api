package com.integrador.svfapi.utils;

public record TokenValidationResult(boolean isValid, String code, TokenType tokenType) {
}
