package com.skpijtk.springboot_boilerplate.dto.response.admin.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckinResumeResponse {
    private int totalMahasiswa;
    private int totalCheckin;
    private int totalBelumCheckin;
    private int totalTelatCheckin;
}
