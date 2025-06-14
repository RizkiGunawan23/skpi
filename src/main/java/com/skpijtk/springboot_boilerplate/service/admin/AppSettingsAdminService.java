package com.skpijtk.springboot_boilerplate.service.admin;

import com.skpijtk.springboot_boilerplate.dto.response.admin.appsettings.AppSettingsRequest;
import com.skpijtk.springboot_boilerplate.dto.response.admin.appsettings.AppSettingsResponse;

public interface AppSettingsAdminService {
    AppSettingsResponse getAppSettings();

    // AppSettingsResponse updateAppSettings(AppSettingsRequest request);
}
