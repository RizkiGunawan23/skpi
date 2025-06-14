package com.skpijtk.springboot_boilerplate.controller.admin;

import com.skpijtk.springboot_boilerplate.constant.ResponseMessage;
import com.skpijtk.springboot_boilerplate.dto.request.admin.auth.LoginAdminRequest;
import com.skpijtk.springboot_boilerplate.dto.request.admin.auth.SignupAdminRequest;
import com.skpijtk.springboot_boilerplate.dto.response.ApiResponse;
import com.skpijtk.springboot_boilerplate.dto.response.admin.auth.LoginAdminResponse;
import com.skpijtk.springboot_boilerplate.dto.response.admin.auth.SignupAdminResponse;
import com.skpijtk.springboot_boilerplate.service.admin.AuthAdminService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1")
public class AuthAdminController {
    @Autowired
    private AuthAdminService authService;

    @PostMapping("/admin/signup")
    public ResponseEntity<ApiResponse<SignupAdminResponse>> signupAdmin(
            @Valid @RequestBody SignupAdminRequest request) {
        SignupAdminResponse responseData = authService.signupAdmin(request);
        ApiResponse<SignupAdminResponse> response = new ApiResponse<>(responseData, ResponseMessage.SIGNUP_SUCCESS);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/admin/login")
    public ResponseEntity<ApiResponse<LoginAdminResponse>> loginAdmin(@Valid @RequestBody LoginAdminRequest request) {
        LoginAdminResponse responseData = authService.loginAdmin(request);
        ApiResponse<LoginAdminResponse> response = new ApiResponse<>(responseData, ResponseMessage.LOGIN_SUCCESS);
        return ResponseEntity.ok(response);
    }
}
