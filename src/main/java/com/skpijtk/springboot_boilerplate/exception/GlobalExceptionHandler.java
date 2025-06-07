package com.skpijtk.springboot_boilerplate.exception;

import java.util.stream.Collectors;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.skpijtk.springboot_boilerplate.dto.response.ApiResponse;
import com.skpijtk.springboot_boilerplate.dto.response.FieldErrorResponse;
import com.skpijtk.springboot_boilerplate.dto.response.ValidationErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
        @ExceptionHandler(ApiException.class)
        public ResponseEntity<ApiResponse<Object>> handleApiException(ApiException ex) {
                ApiResponse<Object> response = new ApiResponse<>(
                                null,
                                ex.getMessage());
                return ResponseEntity.status(ex.getStatusCode()).body(response);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
                List<FieldErrorResponse> errors = ex.getBindingResult().getFieldErrors().stream()
                                .map(error -> new FieldErrorResponse(error.getField(), error.getDefaultMessage()))
                                .collect(Collectors.toList());

                ValidationErrorResponse response = new ValidationErrorResponse(
                                null,
                                errors,
                                HttpStatus.BAD_REQUEST.value(),
                                HttpStatus.BAD_REQUEST.getReasonPhrase());
                return ResponseEntity.badRequest().body(response);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiResponse<Object>> handleAllExceptions(Exception ex) {
                ApiResponse<Object> response = new ApiResponse<>(
                                null,
                                "Internal Server Error. Please try again");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<ApiResponse<Object>> handleRuntimeException(RuntimeException ex) {
                ApiResponse<Object> response = new ApiResponse<>(
                                null,
                                ex.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
}
