package com.skpijtk.springboot_boilerplate.dto.response.admin.appsettings;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AppSettingsResponse {
    private String defaultCheckInTime;
    private String defaultCheckOutTime;
    private String checkInLateToleranceMinutes;
}
