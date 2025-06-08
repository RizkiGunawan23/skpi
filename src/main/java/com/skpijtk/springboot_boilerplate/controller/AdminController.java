package com.skpijtk.springboot_boilerplate.controller;

import com.skpijtk.springboot_boilerplate.dto.response.ApiResponse;
import com.skpijtk.springboot_boilerplate.dto.response.admin.AdminProfileResponse;
import com.skpijtk.springboot_boilerplate.service.AdminService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @GetMapping("/admin/profile")
    public ResponseEntity<ApiResponse<AdminProfileResponse>> getProfile(
            @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        AdminProfileResponse responseData = adminService.getProfile(email);
        ApiResponse<AdminProfileResponse> response = new ApiResponse<>(responseData, "Data successfully displayed");
        return ResponseEntity.ok(response);
    }
}
