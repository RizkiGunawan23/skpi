package com.skpijtk.springboot_boilerplate.service.admin;

import com.skpijtk.springboot_boilerplate.dto.response.admin.common.StudentAttendanceListPageResponse;
import com.skpijtk.springboot_boilerplate.dto.response.admin.dashboard.CheckinResumeResponse;
import com.skpijtk.springboot_boilerplate.dto.response.admin.dashboard.StudentTotalResponse;

public interface DashboardAdminService {
    StudentTotalResponse getStudentTotal();

    CheckinResumeResponse getCheckinResume();

    // StudentAttendanceListPageResponse getStudentCheckinList();

    // void getStudentAttendanceList();
}
