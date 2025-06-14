package com.skpijtk.springboot_boilerplate.exception;

import java.util.stream.Collectors;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.skpijtk.springboot_boilerplate.constant.ResponseMessage;
import com.skpijtk.springboot_boilerplate.dto.response.ApiResponse;
import com.skpijtk.springboot_boilerplate.dto.response.FieldErrorResponse;
import com.skpijtk.springboot_boilerplate.dto.response.ValidationErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
        @ExceptionHandler(NoHandlerFoundException.class)
        public ResponseEntity<ApiResponse<Object>> handleNotFound(NoHandlerFoundException ex) {
                ApiResponse<Object> response = new ApiResponse<>(
                                null,
                                "Endpoint not found: " + ex.getRequestURL());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
        public ResponseEntity<ApiResponse<Object>> handleNotAllowed(HttpRequestMethodNotSupportedException ex) {
                ApiResponse<Object> response = new ApiResponse<>(
                                null,
                                String.format("Http %s method not allowed for this endpoint", ex.getMethod()));
                return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
                List<FieldErrorResponse> errors = ex.getBindingResult().getFieldErrors().stream()
                                .map(error -> new FieldErrorResponse(error.getField(), error.getDefaultMessage()))
                                .sorted((a, b) -> {
                                        int cmp = a.getField().compareToIgnoreCase(b.getField());
                                        if (cmp != 0)
                                                return cmp;
                                        return a.getMessage().compareToIgnoreCase(b.getMessage());
                                })
                                .collect(Collectors.toList());

                ValidationErrorResponse response = new ValidationErrorResponse(null, errors);
                return ResponseEntity.badRequest().body(response);
        }

        @ExceptionHandler(MethodArgumentTypeMismatchException.class)
        public ResponseEntity<ApiResponse<Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
                String param = ex.getName();
                String requiredType;
                Class<?> reqType = ex.getRequiredType();
                if (reqType != null) {
                        requiredType = reqType.getSimpleName();
                } else {
                        requiredType = "unknown";
                }
                Object exValue = ex.getValue();
                String value = (exValue != null) ? exValue.toString() : "null";
                String message = String.format(
                                "Parameter '%s' with value '%s' is not valid. Required type: %s",
                                param, value, requiredType);
                ApiResponse<Object> response = new ApiResponse<>(null, message);
                return ResponseEntity.badRequest().body(response);
        }

        @ExceptionHandler(NumberFormatException.class)
        public ResponseEntity<ApiResponse<Object>> handleNumberFormat(NumberFormatException ex) {
                String msg = ex.getMessage();
                if (msg != null && msg.startsWith("For input string:")) {
                        int firstQuote = msg.indexOf('"');
                        int lastQuote = msg.lastIndexOf('"');
                        if (firstQuote != -1 && lastQuote != -1 && lastQuote > firstQuote) {
                                msg = msg.substring(firstQuote + 1, lastQuote);
                        }
                }
                String message = "Invalid number format: " + msg;
                ApiResponse<Object> response = new ApiResponse<>(null, message);
                return ResponseEntity.badRequest().body(response);
        }

        @ExceptionHandler(ApiException.class)
        public ResponseEntity<?> handleApiException(ApiException ex) {
                if (ex.getFieldErrors() != null) {
                        List<FieldErrorResponse> sortedErrors = ex.getFieldErrors().stream()
                                        .sorted((a, b) -> {
                                                int cmp = a.getField().compareToIgnoreCase(b.getField());
                                                if (cmp != 0)
                                                        return cmp;
                                                return a.getMessage().compareToIgnoreCase(b.getMessage());
                                        })
                                        .collect(Collectors.toList());
                        ValidationErrorResponse response = new ValidationErrorResponse(
                                        null,
                                        sortedErrors);
                        return ResponseEntity.status(ex.getStatusCode()).body(response);
                } else {
                        ApiResponse<Object> response = new ApiResponse<>(
                                        null,
                                        ex.getMessage());
                        return ResponseEntity.status(ex.getStatusCode()).body(response);
                }
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiResponse<Object>> handleAllExceptions(Exception ex) {
                ApiResponse<Object> response = new ApiResponse<>(
                                null,
                                ResponseMessage.INTERNAL_SERVER_ERROR);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
}
