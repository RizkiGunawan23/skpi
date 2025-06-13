package com.skpijtk.springboot_boilerplate.dto.request.admin.dashboard;

import lombok.Data;

@Data
public class StudentListQuery {
    private String student_name;
    private String startdate;
    private String enddate;
    private int page = 0;
    private int size = 10;
}
