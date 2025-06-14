package com.skpijtk.springboot_boilerplate.service.student;

import com.skpijtk.springboot_boilerplate.dto.request.student.attendance.StudentCheckinRequest;
import com.skpijtk.springboot_boilerplate.dto.request.student.attendance.StudentCheckoutRequest;
import com.skpijtk.springboot_boilerplate.dto.response.student.attendance.StudentCheckinResponse;
import com.skpijtk.springboot_boilerplate.dto.response.student.attendance.StudentCheckoutResponse;

public interface AttendanceStudentService {
    StudentCheckinResponse checkinStudent(String email, StudentCheckinRequest request);

    StudentCheckoutResponse checkoutStudent(String email, StudentCheckoutRequest request);
}
