package com.skpijtk.springboot_boilerplate.exception;

import org.springframework.http.HttpStatusCode;

import lombok.Getter;

public class ApiException extends RuntimeException {
    @Getter
    private final HttpStatusCode statusCode;

    public ApiException(String message, HttpStatusCode statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}