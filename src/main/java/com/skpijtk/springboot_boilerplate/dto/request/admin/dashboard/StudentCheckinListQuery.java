package com.skpijtk.springboot_boilerplate.dto.request.admin.dashboard;

import lombok.Data;

@Data
public class StudentCheckinListQuery {
    private String student_name;
    private String startdate;
    private String enddate;
    private String sortBy = "nim";
    private String sortDir = "asc";
    private String page = "0";
    private String size = "10";
}
