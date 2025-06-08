package com.skpijtk.springboot_boilerplate.service.admin.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skpijtk.springboot_boilerplate.dto.response.admin.dashboard.CheckinResumeResponse;
import com.skpijtk.springboot_boilerplate.dto.response.admin.dashboard.StudentTotalResponse;
import com.skpijtk.springboot_boilerplate.model.Attendance;
import com.skpijtk.springboot_boilerplate.repository.AttendanceRepository;
import com.skpijtk.springboot_boilerplate.repository.StudentRepository;
import com.skpijtk.springboot_boilerplate.service.admin.DashboardAdminService;

@Service
public class DashboardAdminServiceImpl implements DashboardAdminService {
    @Autowired
    StudentRepository studentRepository;

    @Autowired
    AttendanceRepository attendanceRepository;

    @Override
    public StudentTotalResponse getStudentTotal() {
        int studentTotal = (int) studentRepository.count();
        return new StudentTotalResponse(studentTotal);
    }

    @Override
    public CheckinResumeResponse getCheckinResume() {
        int studentTotal = (int) studentRepository.count();
        int checkinTotal = (int) attendanceRepository.count();
        int notCheckinTotal = (int) studentRepository.countByAttendanceListIsEmpty();
        int lateCheckinTotal = (int) attendanceRepository.countByCheckInStatus(Attendance.CheckInStatus.TERLAMBAT);

        return new CheckinResumeResponse(studentTotal, checkinTotal, notCheckinTotal, lateCheckinTotal);
    }
}
