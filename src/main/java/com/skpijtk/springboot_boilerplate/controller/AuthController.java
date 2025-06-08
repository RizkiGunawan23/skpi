package com.skpijtk.springboot_boilerplate.controller;

import com.skpijtk.springboot_boilerplate.dto.request.auth.LoginUserRequest;
import com.skpijtk.springboot_boilerplate.dto.request.auth.SignupAdminRequest;
import com.skpijtk.springboot_boilerplate.dto.response.ApiResponse;
import com.skpijtk.springboot_boilerplate.dto.response.auth.LoginUserResponse;
import com.skpijtk.springboot_boilerplate.dto.response.auth.SignupAdminResponse;
import com.skpijtk.springboot_boilerplate.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/admin/signup")
    public ResponseEntity<ApiResponse<SignupAdminResponse>> signupAdmin(
            @Valid @RequestBody SignupAdminRequest request) {
        SignupAdminResponse responseData = authService.signupAdmin(request);
        ApiResponse<SignupAdminResponse> response = new ApiResponse<>(responseData, "Signup successful");
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(response);
    }

    @PostMapping("/admin/login")
    public ResponseEntity<ApiResponse<LoginUserResponse>> loginAdmin(@Valid @RequestBody LoginUserRequest request) {
        LoginUserResponse responseData = authService.loginAdmin(request);
        ApiResponse<LoginUserResponse> response = new ApiResponse<>(responseData, "Login successful");
        return ResponseEntity.ok(response);
    }
}
