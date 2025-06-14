package com.skpijtk.springboot_boilerplate.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidationErrorResponse {
    private Object data;
    private List<FieldErrorResponse> errors;
}