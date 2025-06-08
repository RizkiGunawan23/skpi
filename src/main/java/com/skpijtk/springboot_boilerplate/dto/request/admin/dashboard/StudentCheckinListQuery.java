package com.skpijtk.springboot_boilerplate.dto.request.admin.dashboard;

import lombok.Data;

@Data
public class StudentCheckinListQuery {
    private String student_name;
    private String startDate;
    private String endDate;
    private String sortBy = "nim";
    private String sortDir = "asc";
    private int page = 0;
    private int size = 10;
}
