package com.skpijtk.springboot_boilerplate.service.student;

import com.skpijtk.springboot_boilerplate.dto.request.student.auth.LoginStudentRequest;
import com.skpijtk.springboot_boilerplate.dto.response.student.auth.LoginStudentResponse;

public interface AuthStudentService {
    LoginStudentResponse loginStudent(LoginStudentRequest request);
}
