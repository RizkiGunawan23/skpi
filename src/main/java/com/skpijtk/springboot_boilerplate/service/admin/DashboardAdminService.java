package com.skpijtk.springboot_boilerplate.service.admin;

import com.skpijtk.springboot_boilerplate.dto.request.admin.dashboard.StudentAttendanceListQuery;
import com.skpijtk.springboot_boilerplate.dto.request.admin.dashboard.StudentCheckinListQuery;
import com.skpijtk.springboot_boilerplate.dto.request.admin.dashboard.StudentListQuery;
import com.skpijtk.springboot_boilerplate.dto.response.admin.common.StudentListPageResponse;
import com.skpijtk.springboot_boilerplate.dto.response.admin.dashboard.CheckinResumeResponse;
import com.skpijtk.springboot_boilerplate.dto.response.admin.dashboard.StudentTotalResponse;

public interface DashboardAdminService {
    StudentTotalResponse getStudentTotal();

    CheckinResumeResponse getCheckinResume();

    StudentListPageResponse getStudentCheckinList(StudentCheckinListQuery query);

    StudentListPageResponse getStudentAttendanceList(StudentAttendanceListQuery query);

    StudentListPageResponse getStudentList(StudentListQuery query);
}
