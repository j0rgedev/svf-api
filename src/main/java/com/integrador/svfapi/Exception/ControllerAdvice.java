package com.integrador.svfapi.Exception;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorDTO> handleException(BusinessException e) {
        ErrorDTO errorDTO = new ErrorDTO(e.getStatus().value(), e.getMessage());
        return ResponseEntity.status(e.getStatus()).body(errorDTO);
    }

}
