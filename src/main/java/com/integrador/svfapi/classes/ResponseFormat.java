package com.integrador.svfapi.classes;

import java.util.Objects;

public record ResponseFormat(Integer status, String message, Object data) {

    public ResponseFormat(Integer status, String message, Object data) {
        this.status = status;
        Objects.requireNonNull(status);
        this.message = message;
        Objects.requireNonNull(message);
        this.data = data;
        Objects.requireNonNull(data);
    }
}
