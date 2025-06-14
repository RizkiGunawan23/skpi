package com.skpijtk.springboot_boilerplate.dto.response.admin.appsettings;

import lombok.Data;

@Data
public class AppSettingsRequest {
    private String defaultCheckInTime;
    private String defaultCheckOutTime;
    private String checkInLateToleranceMinutes;
}
