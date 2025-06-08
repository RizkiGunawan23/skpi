package com.skpijtk.springboot_boilerplate.dto.response.admin.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AttendanceDataResponse {
    private Long attendanceId;
    private String checkinTime;
    private String checkoutTime;
    private String attendanceDate;
    private Boolean late;
    private String notesCheckin;
    private String notesCheckout;
    private String status;
}
