package com.skpijtk.springboot_boilerplate.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skpijtk.springboot_boilerplate.dto.request.admin.dashboard.StudentListQuery;
import com.skpijtk.springboot_boilerplate.dto.response.ApiResponse;
import com.skpijtk.springboot_boilerplate.dto.response.admin.common.StudentListPageResponse;
import com.skpijtk.springboot_boilerplate.dto.response.admin.dashboard.StudentDetailResponse;
import com.skpijtk.springboot_boilerplate.service.admin.StudentManagementService;

@RestController
@RequestMapping("/api/v1/admin/")
public class StudentManagementController {
    @Autowired
    private StudentManagementService studentManagementService;

    @GetMapping("list_all_mahasiswa")
    public ResponseEntity<ApiResponse<StudentListPageResponse>> getStudentList(
            @ModelAttribute StudentListQuery query) {
        StudentListPageResponse responseData = studentManagementService.getStudentList(query);
        ApiResponse<StudentListPageResponse> response = new ApiResponse<StudentListPageResponse>(
                responseData,
                "Data successfully displayed");
        return ResponseEntity.ok(response);
    }

    @GetMapping("mahasiswa/{id}")
    public ResponseEntity<ApiResponse<StudentDetailResponse>> getStudentDetail(@PathVariable("id") Long studentId) {
        StudentDetailResponse responseData = studentManagementService.getStudentDetail(studentId);
        ApiResponse<StudentDetailResponse> response = new ApiResponse<StudentDetailResponse>(
                responseData,
                "Student data successfully found");
        return ResponseEntity.ok(response);
    }
}
