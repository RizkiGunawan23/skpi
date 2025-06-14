package com.skpijtk.springboot_boilerplate.dto.request.admin.dashboard;

import lombok.Data;

@Data
public class StudentAttendanceListQuery {
    private String student_id;
    private String startdate;
    private String enddate;
    private String page = "0";
    private String size = "10";
}
