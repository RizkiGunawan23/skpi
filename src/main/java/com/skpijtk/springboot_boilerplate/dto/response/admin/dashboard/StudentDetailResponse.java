package com.skpijtk.springboot_boilerplate.dto.response.admin.dashboard;

import java.util.List;

import com.skpijtk.springboot_boilerplate.dto.response.admin.common.AttendanceDataResponse;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentDetailResponse {
    private int studentId;
    private int userId;
    private String studentName;
    private String nim;
    private String email;
    private List<AttendanceDataResponse> attendanceData;
}
