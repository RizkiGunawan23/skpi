package com.skpijtk.springboot_boilerplate.dto.response.student.profile;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentProfileResponse {
    private int studentId;
    private String studentName;
    private String nim;
    private List<AttendanceDataResponse> attendanceData;
}
