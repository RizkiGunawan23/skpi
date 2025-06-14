package com.skpijtk.springboot_boilerplate.controller.admin;

import com.skpijtk.springboot_boilerplate.constant.ResponseMessage;
import com.skpijtk.springboot_boilerplate.dto.response.ApiResponse;
import com.skpijtk.springboot_boilerplate.dto.response.admin.profile.AdminProfileResponse;
import com.skpijtk.springboot_boilerplate.service.admin.ProfileAdminService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1")
public class ProfileAdminController {
    @Autowired
    private ProfileAdminService adminService;

    @GetMapping("/admin/profile")
    public ResponseEntity<ApiResponse<AdminProfileResponse>> getAdminProfile(
            @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        AdminProfileResponse responseData = adminService.getAdminProfile(email);
        ApiResponse<AdminProfileResponse> response = new ApiResponse<>(responseData,
                ResponseMessage.DATA_DISPLAY_SUCCESS);
        return ResponseEntity.ok(response);
    }
}
