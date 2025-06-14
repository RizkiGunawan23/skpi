package com.skpijtk.springboot_boilerplate.dto.response;

import com.skpijtk.springboot_boilerplate.constant.ResponseMessage;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentProfileApiResponse<T> {
    private T data;
    private int totalData;
    private int totalPage;
    private int currentPage;
    private int pageSize;
    private String message;

    public StudentProfileApiResponse(T data, int totalData, int totalPage, int currentPage, int pageSize,
            ResponseMessage message) {
        this.data = data;
        this.totalData = totalData;
        this.totalPage = totalPage;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.message = message.toString();
    }
}