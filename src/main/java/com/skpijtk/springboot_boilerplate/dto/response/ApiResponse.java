package com.skpijtk.springboot_boilerplate.dto.response;

import com.skpijtk.springboot_boilerplate.constant.ResponseMessage;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private T data;
    private String message;

    public ApiResponse(T data, ResponseMessage message) {
        this.data = data;
        this.message = message.toString();
    }
}