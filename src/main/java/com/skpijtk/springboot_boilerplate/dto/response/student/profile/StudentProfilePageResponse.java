package com.skpijtk.springboot_boilerplate.dto.response.student.profile;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentProfilePageResponse {
    private StudentProfileResponse data;
    private int totalData;
    private int totalPage;
    private int currentPage;
    private int pageSize;
}
