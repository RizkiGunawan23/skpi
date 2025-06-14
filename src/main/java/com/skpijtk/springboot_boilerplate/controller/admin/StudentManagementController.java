package com.skpijtk.springboot_boilerplate.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skpijtk.springboot_boilerplate.constant.ResponseMessage;
import com.skpijtk.springboot_boilerplate.dto.request.admin.studentmanagement.CreateStudentRequest;
import com.skpijtk.springboot_boilerplate.dto.request.admin.studentmanagement.StudentListQuery;
import com.skpijtk.springboot_boilerplate.dto.request.admin.studentmanagement.UpdateStudentRequest;
import com.skpijtk.springboot_boilerplate.dto.response.ApiResponse;
import com.skpijtk.springboot_boilerplate.dto.response.admin.common.StudentListPageResponse;
import com.skpijtk.springboot_boilerplate.dto.response.admin.studentmanagement.DeleteStudentResponse;
import com.skpijtk.springboot_boilerplate.dto.response.admin.studentmanagement.StudentResponse;
import com.skpijtk.springboot_boilerplate.service.admin.StudentManagementService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

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
                                ResponseMessage.DATA_DISPLAY_SUCCESS);
                return ResponseEntity.ok(response);
        }

        @GetMapping("mahasiswa/{id}")
        public ResponseEntity<ApiResponse<StudentResponse>> getStudentDetail(@PathVariable("id") Long studentId) {
                StudentResponse responseData = studentManagementService.getStudentDetail(studentId);
                ApiResponse<StudentResponse> response = new ApiResponse<StudentResponse>(
                                responseData,
                                ResponseMessage.STUDENT_FOUND);
                return ResponseEntity.ok(response);
        }

        @PostMapping("add-mahasiswa")
        public ResponseEntity<ApiResponse<StudentResponse>> createStudent(
                        @Valid @RequestBody CreateStudentRequest request) {
                StudentResponse responseData = studentManagementService.createStudent(request);
                ApiResponse<StudentResponse> response = new ApiResponse<>(
                                responseData,
                                ResponseMessage.DATA_SAVE_SUCCESS);
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        @PutMapping("edit-mahasiswa/{id}")
        public ResponseEntity<ApiResponse<StudentResponse>> updateStudent(@PathVariable("id") Long studentId,
                        @Valid @RequestBody UpdateStudentRequest request) {
                StudentResponse responseData = studentManagementService.updateStudent(request, studentId);
                ApiResponse<StudentResponse> response = new ApiResponse<>(
                                responseData,
                                ResponseMessage.UPDATE_STUDENT_SUCCESS.format(responseData.getStudentName()));
                return ResponseEntity.ok(response);
        }

        @DeleteMapping("mahasiswa/{id}")
        public ResponseEntity<ApiResponse<DeleteStudentResponse>> deleteStudent(@PathVariable("id") Long studentId) {
                DeleteStudentResponse responseData = studentManagementService.deleteStudent(studentId);
                ApiResponse<DeleteStudentResponse> response = new ApiResponse<>(
                                responseData,
                                ResponseMessage.DELETE_STUDENT_SUCCESS.format(responseData.getStudent_name()));
                return ResponseEntity.ok(response);
        }
}
