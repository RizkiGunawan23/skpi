package com.skpijtk.springboot_boilerplate.dto.response.admin.common;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentAttendanceListPageResponse {
    private List<StudentAttendanceListResponse> data;
    private int totalData;
    private int totalPage;
    private int currentPage;
    private int pageSize;
}
