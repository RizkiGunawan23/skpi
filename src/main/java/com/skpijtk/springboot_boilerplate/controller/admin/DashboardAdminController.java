package com.skpijtk.springboot_boilerplate.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skpijtk.springboot_boilerplate.constant.ResponseMessage;
import com.skpijtk.springboot_boilerplate.dto.request.admin.dashboard.StudentAttendanceListQuery;
import com.skpijtk.springboot_boilerplate.dto.request.admin.dashboard.StudentCheckinListQuery;
import com.skpijtk.springboot_boilerplate.dto.response.ApiResponse;
import com.skpijtk.springboot_boilerplate.dto.response.admin.common.StudentListPageResponse;
import com.skpijtk.springboot_boilerplate.dto.response.admin.dashboard.CheckinResumeResponse;
import com.skpijtk.springboot_boilerplate.dto.response.admin.dashboard.StudentTotalResponse;
import com.skpijtk.springboot_boilerplate.service.admin.DashboardAdminService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@RestController
@RequestMapping("/api/v1/admin/")
public class DashboardAdminController {
        @Autowired
        private DashboardAdminService dashboardAdminService;

        @GetMapping("total_mahasiswa")
        public ResponseEntity<ApiResponse<StudentTotalResponse>> getStudentTotal() {
                StudentTotalResponse responseData = dashboardAdminService.getStudentTotal();
                ApiResponse<StudentTotalResponse> response = new ApiResponse<StudentTotalResponse>(responseData,
                                ResponseMessage.DATA_DISPLAY_SUCCESS);
                return ResponseEntity.ok(response);
        }

        @GetMapping("resume_checkin")
        public ResponseEntity<ApiResponse<CheckinResumeResponse>> getCheckinResume() {
                CheckinResumeResponse responseData = dashboardAdminService.getCheckinResume();
                ApiResponse<CheckinResumeResponse> response = new ApiResponse<CheckinResumeResponse>(responseData,
                                ResponseMessage.DATA_DISPLAY_SUCCESS);
                return ResponseEntity.ok(response);
        }

        @GetMapping("list_checkin_mahasiswa")
        public ResponseEntity<ApiResponse<StudentListPageResponse>> getStudentCheckinList(
                        @ModelAttribute StudentCheckinListQuery query) {
                StudentListPageResponse responseData = dashboardAdminService.getStudentCheckinList(query);
                ApiResponse<StudentListPageResponse> response = new ApiResponse<StudentListPageResponse>(
                                responseData,
                                ResponseMessage.DATA_DISPLAY_SUCCESS);
                return ResponseEntity.ok(response);
        }

        @GetMapping("list_attendance_mahasiswa")
        public ResponseEntity<ApiResponse<StudentListPageResponse>> getStudentAttendanceList(
                        @ModelAttribute StudentAttendanceListQuery query) {
                StudentListPageResponse responseData = dashboardAdminService.getStudentAttendanceList(query);
                ApiResponse<StudentListPageResponse> response = new ApiResponse<StudentListPageResponse>(
                                responseData,
                                ResponseMessage.DATA_DISPLAY_SUCCESS);
                return ResponseEntity.ok(response);
        }
}
