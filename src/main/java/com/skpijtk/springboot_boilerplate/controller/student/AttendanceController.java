package com.skpijtk.springboot_boilerplate.controller.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skpijtk.springboot_boilerplate.constant.ResponseMessage;
import com.skpijtk.springboot_boilerplate.dto.request.student.attendance.StudentCheckinRequest;
import com.skpijtk.springboot_boilerplate.dto.request.student.attendance.StudentCheckoutRequest;
import com.skpijtk.springboot_boilerplate.dto.response.ApiResponse;
import com.skpijtk.springboot_boilerplate.dto.response.student.attendance.StudentCheckinResponse;
import com.skpijtk.springboot_boilerplate.dto.response.student.attendance.StudentCheckoutResponse;
import com.skpijtk.springboot_boilerplate.service.student.AttendanceStudentService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/mahasiswa/")
public class AttendanceController {
    @Autowired
    private AttendanceStudentService attendanceStudentService;

    @PostMapping("checkin")
    public ResponseEntity<ApiResponse<StudentCheckinResponse>> studentCheckin(
            @AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody StudentCheckinRequest request) {
        StudentCheckinResponse responseData = attendanceStudentService.checkinStudent(userDetails.getUsername(),
                request);
        ApiResponse<StudentCheckinResponse> response = new ApiResponse<>(responseData, ResponseMessage.CHECKIN_SUCCESS);
        return ResponseEntity.ok(response);
    }

    @PostMapping("checkout")
    public ResponseEntity<ApiResponse<StudentCheckoutResponse>> studentCheckin(
            @AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody StudentCheckoutRequest request) {
        StudentCheckoutResponse responseData = attendanceStudentService.checkoutStudent(userDetails.getUsername(),
                request);
        ApiResponse<StudentCheckoutResponse> response = new ApiResponse<>(responseData,
                ResponseMessage.CHECKOUT_SUCCESS);
        return ResponseEntity.ok(response);
    }
}
