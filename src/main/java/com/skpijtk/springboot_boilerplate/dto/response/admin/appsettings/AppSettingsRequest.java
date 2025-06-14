package com.skpijtk.springboot_boilerplate.dto.response.admin.appsettings;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AppSettingsRequest {
    @NotBlank(message = "Default check-in time cannot be blank")
    @Pattern(regexp = "^\\d{2}:\\d{2}:\\d{2}$", message = "Format must be HH:MM:SS")
    private String defaultCheckInTime;

    @NotBlank(message = "Default check-out time cannot be blank")
    @Pattern(regexp = "^\\d{2}:\\d{2}:\\d{2}$", message = "Format must be HH:MM:SS")
    private String defaultCheckOutTime;

    @NotBlank(message = "Check-in late tolerance minutes cannot be blank")
    @NotNull(message = "Check-in late tolerance minutes cannot be null")
    @Pattern(regexp = "^[0-9]+$", message = "Late tolerance must be an integer")
    private String checkInLateToleranceMinutes;
}
