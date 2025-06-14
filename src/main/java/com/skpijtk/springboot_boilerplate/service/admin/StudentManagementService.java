package com.skpijtk.springboot_boilerplate.service.admin;

import com.skpijtk.springboot_boilerplate.dto.request.admin.studentmanagement.CreateStudentRequest;
import com.skpijtk.springboot_boilerplate.dto.request.admin.studentmanagement.StudentListQuery;
import com.skpijtk.springboot_boilerplate.dto.request.admin.studentmanagement.UpdateStudentRequest;
import com.skpijtk.springboot_boilerplate.dto.response.admin.common.StudentListPageResponse;
import com.skpijtk.springboot_boilerplate.dto.response.admin.studentmanagement.DeleteStudentResponse;
import com.skpijtk.springboot_boilerplate.dto.response.admin.studentmanagement.StudentResponse;

public interface StudentManagementService {
    StudentListPageResponse getStudentList(StudentListQuery query);

    StudentResponse getStudentDetail(Long studentId);

    StudentResponse createStudent(CreateStudentRequest request);

    StudentResponse updateStudent(UpdateStudentRequest request, Long studentId);

    DeleteStudentResponse deleteStudent(Long studentId);
}
