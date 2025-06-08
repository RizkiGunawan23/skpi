package com.skpijtk.springboot_boilerplate.dto.response.admin.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentAttendanceListResponse {
    private int studentId;
    private int userId;
    private String studentName;
    private String nim;
    private String email;
    private AttendanceDataResponse attendanceData;
}
