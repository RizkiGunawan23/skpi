package com.skpijtk.springboot_boilerplate.service.admin;

import com.skpijtk.springboot_boilerplate.dto.request.admin.dashboard.StudentListQuery;
import com.skpijtk.springboot_boilerplate.dto.response.admin.common.StudentListPageResponse;
import com.skpijtk.springboot_boilerplate.dto.response.admin.dashboard.StudentDetailResponse;

public interface StudentManagementService {
    StudentListPageResponse getStudentList(StudentListQuery query);

    StudentDetailResponse getStudentDetail(Long studentId);
}
