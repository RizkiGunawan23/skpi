package com.skpijtk.springboot_boilerplate.dto.request.admin.dashboard;

import lombok.Data;

@Data
public class StudentAttendanceListQuery {
    private String student_id;
    private String startdate;
    private String enddate;
    private int page = 0;
    private int size = 10;
}
