package com.skpijtk.springboot_boilerplate.dto.response;

import com.skpijtk.springboot_boilerplate.constant.ResponseMessage;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FieldErrorResponse {
    private String field;
    private String message;

    public FieldErrorResponse(String field, ResponseMessage message) {
        this.field = field;
        this.message = message.toString();
    }
}