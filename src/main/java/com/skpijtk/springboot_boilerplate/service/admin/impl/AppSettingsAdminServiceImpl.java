package com.skpijtk.springboot_boilerplate.service.admin.impl;

import java.sql.Time;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skpijtk.springboot_boilerplate.constant.ResponseMessage;
import com.skpijtk.springboot_boilerplate.dto.response.FieldErrorResponse;
import com.skpijtk.springboot_boilerplate.dto.response.admin.appsettings.AppSettingsRequest;
import com.skpijtk.springboot_boilerplate.dto.response.admin.appsettings.AppSettingsResponse;
import com.skpijtk.springboot_boilerplate.exception.ApiException;
import com.skpijtk.springboot_boilerplate.model.AppSettings;
import com.skpijtk.springboot_boilerplate.repository.AppSettingsRepository;
import com.skpijtk.springboot_boilerplate.service.admin.AppSettingsAdminService;
import com.skpijtk.springboot_boilerplate.validation.AppSettingsValidator;

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

    @Override
    @Transactional
    public AppSettingsResponse updateAppSettings(AppSettingsRequest request) {
        List<FieldErrorResponse> errors = AppSettingsValidator.validate(request);
        if (!errors.isEmpty())
            throw new ApiException(errors);

        AppSettings appSettings = appSettingsRepository.findById(1)
                .orElseThrow(
                        () -> new ApiException(HttpStatus.NOT_FOUND, ResponseMessage.APP_SETTINGS_NOT_FOUND));

        appSettings.setDefaultCheckInTime(Time.valueOf(request.getDefaultCheckInTime()));
        appSettings.setDefaultCheckOutTime(Time.valueOf(request.getDefaultCheckOutTime()));
        appSettings.setCheckInLateToleranceMinutes(Integer.parseInt(request.getCheckInLateToleranceMinutes()));
        appSettingsRepository.save(appSettings);

        return new AppSettingsResponse(appSettings.getDefaultCheckInTime().toString(),
                appSettings.getDefaultCheckOutTime().toString(),
                appSettings.getCheckInLateToleranceMinutes().toString());
    }
}
