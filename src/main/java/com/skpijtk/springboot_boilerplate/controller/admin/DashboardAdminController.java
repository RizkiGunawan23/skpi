package com.skpijtk.springboot_boilerplate.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skpijtk.springboot_boilerplate.dto.request.admin.dashboard.StudentCheckinListQuery;
import com.skpijtk.springboot_boilerplate.dto.response.ApiResponse;
import com.skpijtk.springboot_boilerplate.dto.response.admin.common.StudentAttendanceListPageResponse;
import com.skpijtk.springboot_boilerplate.dto.response.admin.dashboard.CheckinResumeResponse;
import com.skpijtk.springboot_boilerplate.dto.response.admin.dashboard.StudentTotalResponse;
import com.skpijtk.springboot_boilerplate.service.admin.DashboardAdminService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@RestController
@RequestMapping("/api/v1")
public class DashboardAdminController {
    @Autowired
    private DashboardAdminService dashboardAdminService;

    @GetMapping("/admin/total_mahasiswa")
    public ResponseEntity<ApiResponse<StudentTotalResponse>> getStudentTotal() {
        StudentTotalResponse responseData = dashboardAdminService.getStudentTotal();
        ApiResponse<StudentTotalResponse> response = new ApiResponse<StudentTotalResponse>(responseData,
                "Data successfully displayed");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin/resume_checkin")
    public ResponseEntity<ApiResponse<CheckinResumeResponse>> getCheckinResume() {
        CheckinResumeResponse responseData = dashboardAdminService.getCheckinResume();
        ApiResponse<CheckinResumeResponse> response = new ApiResponse<CheckinResumeResponse>(responseData,
                "Data successfully displayed");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin/list_checkin_mahasiswa")
    public ResponseEntity<ApiResponse<StudentAttendanceListPageResponse>> getSgetStudentCheckinListtudentCheckin(
            @ModelAttribute StudentCheckinListQuery query) {
        StudentAttendanceListPageResponse responseData = dashboardAdminService.getStudentCheckinList(query);
        ApiResponse<StudentAttendanceListPageResponse> response = new ApiResponse<StudentAttendanceListPageResponse>(
                responseData,
                "Data successfully displayed");
        return ResponseEntity.ok(response);
    }
}
