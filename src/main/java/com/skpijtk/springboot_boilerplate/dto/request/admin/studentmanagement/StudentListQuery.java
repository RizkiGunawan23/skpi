package com.skpijtk.springboot_boilerplate.dto.request.admin.studentmanagement;

import lombok.Data;

@Data
public class StudentListQuery {
    private String student_name;
    private String startdate;
    private String enddate;
    private String page = "0";
    private String size = "10";
}
