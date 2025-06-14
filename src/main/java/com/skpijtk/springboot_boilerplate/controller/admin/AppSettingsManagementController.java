package com.skpijtk.springboot_boilerplate.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skpijtk.springboot_boilerplate.constant.ResponseMessage;
import com.skpijtk.springboot_boilerplate.dto.response.ApiResponse;
import com.skpijtk.springboot_boilerplate.dto.response.admin.appsettings.AppSettingsResponse;
import com.skpijtk.springboot_boilerplate.service.admin.AppSettingsAdminService;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1/admin/system-settings")
public class AppSettingsManagementController {
    @Autowired
    private AppSettingsAdminService appSettingsAdminService;

    @GetMapping
    public ResponseEntity<ApiResponse<AppSettingsResponse>> getAppSettings() {
        AppSettingsResponse appSettingsResponse = appSettingsAdminService.getAppSettings();
        ApiResponse<AppSettingsResponse> response = new ApiResponse<>(appSettingsResponse,
                ResponseMessage.DATA_DISPLAY_SUCCESS);
        return ResponseEntity.ok(response);
    }
}
