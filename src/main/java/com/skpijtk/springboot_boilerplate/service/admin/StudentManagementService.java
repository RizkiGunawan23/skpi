package com.skpijtk.springboot_boilerplate.service.admin;

public interface StudentManagementService {
    void getStudentList();

    void getStudentById();

    void createStudent();

    void updateStudent();

    void deleteStudent();
}
