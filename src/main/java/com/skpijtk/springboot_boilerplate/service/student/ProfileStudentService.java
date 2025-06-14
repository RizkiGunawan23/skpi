package com.skpijtk.springboot_boilerplate.service.student;

import com.skpijtk.springboot_boilerplate.dto.request.student.profile.StudentProfileQuery;
import com.skpijtk.springboot_boilerplate.dto.response.student.profile.StudentProfilePageResponse;

public interface ProfileStudentService {
    StudentProfilePageResponse getStudentProfile(String email, StudentProfileQuery query);
}
