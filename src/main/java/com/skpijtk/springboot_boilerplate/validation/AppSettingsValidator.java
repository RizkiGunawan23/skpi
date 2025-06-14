package com.skpijtk.springboot_boilerplate.validation;

import com.skpijtk.springboot_boilerplate.dto.response.FieldErrorResponse;
import com.skpijtk.springboot_boilerplate.dto.response.admin.appsettings.AppSettingsRequest;

import java.util.ArrayList;
import java.util.List;

public class AppSettingsValidator {
    public static List<FieldErrorResponse> validate(AppSettingsRequest req) {
        List<FieldErrorResponse> errors = new ArrayList<>();

        // Validasi defaultCheckInTime
        if (req.getDefaultCheckInTime() == null || req.getDefaultCheckInTime().isBlank()) {
            errors.add(new FieldErrorResponse("defaultCheckInTime", "Default check-in time is required"));
        } else if (!req.getDefaultCheckInTime().matches("^\\d{2}:\\d{2}:\\d{2}$")) {
            errors.add(new FieldErrorResponse("defaultCheckInTime", "Format must be HH:MM:SS"));
        } else {
            String[] parts = req.getDefaultCheckInTime().split(":");
            int hour = Integer.parseInt(parts[0]);
            int minute = Integer.parseInt(parts[1]);
            int second = Integer.parseInt(parts[2]);
            if (hour < 0 || hour > 23)
                errors.add(new FieldErrorResponse("defaultCheckInTime", "Hour must be between 00 and 23"));
            if (minute < 0 || minute > 59)
                errors.add(new FieldErrorResponse("defaultCheckInTime", "Minute must be between 00 and 59"));
            if (second < 0 || second > 59)
                errors.add(new FieldErrorResponse("defaultCheckInTime", "Second must be between 00 and 59"));
        }

        // Validasi defaultCheckOutTime
        if (req.getDefaultCheckOutTime() == null || req.getDefaultCheckOutTime().isBlank()) {
            errors.add(new FieldErrorResponse("defaultCheckOutTime", "Default check-out time is required"));
        } else if (!req.getDefaultCheckOutTime().matches("^\\d{2}:\\d{2}:\\d{2}$")) {
            errors.add(new FieldErrorResponse("defaultCheckOutTime", "Format must be HH:MM:SS"));
        } else {
            String[] parts = req.getDefaultCheckOutTime().split(":");
            int hour = Integer.parseInt(parts[0]);
            int minute = Integer.parseInt(parts[1]);
            int second = Integer.parseInt(parts[2]);
            if (hour < 0 || hour > 23)
                errors.add(new FieldErrorResponse("defaultCheckOutTime", "Hour must be between 00 and 23"));
            if (minute < 0 || minute > 59)
                errors.add(new FieldErrorResponse("defaultCheckOutTime", "Minute must be between 00 and 59"));
            if (second < 0 || second > 59)
                errors.add(new FieldErrorResponse("defaultCheckOutTime", "Second must be between 00 and 59"));
        }

        // Validasi checkInLateToleranceMinutes
        if (req.getCheckInLateToleranceMinutes() == null || req.getCheckInLateToleranceMinutes().isBlank()) {
            errors.add(new FieldErrorResponse("checkInLateToleranceMinutes", "Late tolerance is required"));
        } else {
            try {
                int val = Integer.parseInt(req.getCheckInLateToleranceMinutes());
                if (val < 0 || val > 1440) {
                    errors.add(new FieldErrorResponse("checkInLateToleranceMinutes",
                            "Late tolerance must be between 0 and 1440"));
                }
            } catch (Exception e) {
                errors.add(new FieldErrorResponse("checkInLateToleranceMinutes",
                        "Late tolerance must be a valid integer"));
            }
        }

        return errors;
    }
}