package com.skpijtk.springboot_boilerplate.dto.response.admin.profile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import lombok.Data;

@Data
public class AdminProfileResponse {
    private String name;
    private String role;
    private String time;

    public AdminProfileResponse(String name, String role) {
        this.name = name;
        this.role = role;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy",
                new Locale("id", "ID"));
        this.time = LocalDateTime.now().format(formatter);
    }
}
