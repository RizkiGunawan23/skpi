package com.skpijtk.springboot_boilerplate.exception;

import java.util.List;

import org.springframework.http.HttpStatus;

import com.skpijtk.springboot_boilerplate.constant.ResponseMessage;
import com.skpijtk.springboot_boilerplate.dto.response.FieldErrorResponse;

import lombok.Getter;

public class ApiException extends RuntimeException {
    @Getter
    private final HttpStatus statusCode;

    @Getter
    private final List<FieldErrorResponse> fieldErrors;

    public ApiException(HttpStatus statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
        this.fieldErrors = null;
    }

    public ApiException(HttpStatus statusCode, ResponseMessage message) {
        super(message.toString());
        this.statusCode = statusCode;
        this.fieldErrors = null;
    }

    public ApiException(List<FieldErrorResponse> fieldErrors) {
        super("Validation failed");
        this.statusCode = HttpStatus.BAD_REQUEST;
        this.fieldErrors = fieldErrors;
    }
}