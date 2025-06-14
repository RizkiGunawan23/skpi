package com.skpijtk.springboot_boilerplate.controller.student;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skpijtk.springboot_boilerplate.constant.ResponseMessage;
import com.skpijtk.springboot_boilerplate.dto.request.student.auth.LoginStudentRequest;
import com.skpijtk.springboot_boilerplate.dto.response.ApiResponse;
import com.skpijtk.springboot_boilerplate.dto.response.student.auth.LoginStudentResponse;
import com.skpijtk.springboot_boilerplate.service.student.AuthStudentService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/mahasiswa/")
public class AuthStudentController {
    @Autowired
    private AuthStudentService authStudentService;

    @PostMapping("login")
    public ResponseEntity<ApiResponse<LoginStudentResponse>> loginStudent(
            @Valid @RequestBody LoginStudentRequest request) {
        LoginStudentResponse responseData = authStudentService.loginStudent(request);
        ApiResponse<LoginStudentResponse> response = new ApiResponse<>(responseData, ResponseMessage.LOGIN_SUCCESS);
        return ResponseEntity.ok(response);
    }
}
