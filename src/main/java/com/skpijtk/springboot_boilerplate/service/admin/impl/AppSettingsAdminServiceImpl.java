package com.skpijtk.springboot_boilerplate.service.admin.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.skpijtk.springboot_boilerplate.constant.ResponseMessage;
import com.skpijtk.springboot_boilerplate.dto.response.admin.appsettings.AppSettingsResponse;
import com.skpijtk.springboot_boilerplate.exception.ApiException;
import com.skpijtk.springboot_boilerplate.model.AppSettings;
import com.skpijtk.springboot_boilerplate.repository.AppSettingsRepository;
import com.skpijtk.springboot_boilerplate.service.admin.AppSettingsAdminService;

@Service
public class AppSettingsAdminServiceImpl implements AppSettingsAdminService {
    @Autowired
    private AppSettingsRepository appSettingsRepository;

    @Override
    public AppSettingsResponse getAppSettings() {
        AppSettings appSettings = appSettingsRepository.findById(1)
                .orElseThrow(
                        () -> new ApiException(HttpStatus.NOT_FOUND, ResponseMessage.APP_SETTINGS_NOT_FOUND));
        return new AppSettingsResponse(appSettings.getDefaultCheckInTime().toString(),
                appSettings.getDefaultCheckOutTime().toString(),
                appSettings.getCheckInLateToleranceMinutes().toString());
    }
}
