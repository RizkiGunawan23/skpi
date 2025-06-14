package com.skpijtk.springboot_boilerplate.dto.request.student.profile;

import lombok.Data;

@Data
public class StudentProfileQuery {
    private String startdate;
    private String enddate;
    private String page = "0";
    private String size = "10";
}