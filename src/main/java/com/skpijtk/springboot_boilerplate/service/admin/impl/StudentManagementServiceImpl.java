package com.skpijtk.springboot_boilerplate.service.admin.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.skpijtk.springboot_boilerplate.dto.request.admin.dashboard.StudentListQuery;
import com.skpijtk.springboot_boilerplate.dto.response.FieldErrorResponse;
import com.skpijtk.springboot_boilerplate.dto.response.admin.common.AttendanceDataResponse;
import com.skpijtk.springboot_boilerplate.dto.response.admin.common.StudentListPageResponse;
import com.skpijtk.springboot_boilerplate.dto.response.admin.common.StudentListResponse;
import com.skpijtk.springboot_boilerplate.dto.response.admin.dashboard.StudentDetailResponse;
import com.skpijtk.springboot_boilerplate.exception.ApiException;
import com.skpijtk.springboot_boilerplate.model.Attendance;
import com.skpijtk.springboot_boilerplate.model.Student;
import com.skpijtk.springboot_boilerplate.repository.AttendanceRepository;
import com.skpijtk.springboot_boilerplate.repository.StudentRepository;
import com.skpijtk.springboot_boilerplate.service.admin.StudentManagementService;
import com.skpijtk.springboot_boilerplate.validation.StudentListValidator;

@Service
public class StudentManagementServiceImpl implements StudentManagementService {
    @Autowired
    StudentRepository studentRepository;

    @Autowired
    AttendanceRepository attendanceRepository;

    @Override
    public StudentListPageResponse getStudentList(StudentListQuery query) {
        List<FieldErrorResponse> errors = StudentListValidator.validate(query);
        if (!errors.isEmpty())
            throw new ApiException(errors);

        String studentNameQuery = query.getStudent_name();
        LocalDate start = null, end = null;
        if (query.getStartdate() != null && !query.getStartdate().isBlank()) {
            start = LocalDate.parse(query.getStartdate());
        }
        if (query.getEnddate() != null && !query.getEnddate().isBlank()) {
            end = LocalDate.parse(query.getEnddate());
        }
        int page = query.getPage() != null && !query.getPage().isBlank() ? Integer.parseInt(query.getPage()) : 0;
        int size = query.getSize() != null && !query.getSize().isBlank() ? Integer.parseInt(query.getSize()) : 10;

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));

        Page<Attendance> attendancePage;
        if (studentNameQuery != null && !studentNameQuery.isBlank()) {
            if (start != null && end != null) {
                attendancePage = attendanceRepository
                        .findAllByStudent_User_NameIgnoreCaseContainingAndAttendanceDateBetween(
                                studentNameQuery, java.sql.Date.valueOf(start), java.sql.Date.valueOf(end), pageable);
            } else if (start != null) {
                attendancePage = attendanceRepository
                        .findAllByStudent_User_NameIgnoreCaseContainingAndAttendanceDateBetween(
                                studentNameQuery, java.sql.Date.valueOf(start), java.sql.Date.valueOf(start), pageable);
            } else {
                attendancePage = attendanceRepository.findAllByStudent_User_NameIgnoreCaseContaining(studentNameQuery,
                        pageable);
            }
        } else if (start != null && end != null) {
            attendancePage = attendanceRepository
                    .findAllByAttendanceDateBetween(
                            java.sql.Date.valueOf(start), java.sql.Date.valueOf(end), pageable);
        } else if (start != null) {
            attendancePage = attendanceRepository
                    .findAllByAttendanceDateBetween(
                            java.sql.Date.valueOf(start), java.sql.Date.valueOf(start), pageable);
        } else {
            attendancePage = attendanceRepository.findAll(pageable);
        }

        if (attendancePage.getContent().isEmpty())
            throw new ApiException("Student data not found.", HttpStatus.NOT_FOUND);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<StudentListResponse> data = new ArrayList<>();
        for (Attendance attendance : attendancePage.getContent()) {
            Student student = attendance.getStudent();

            String status = attendance.getCheckInStatus() != null ? attendance.getCheckInStatus().name()
                    : "BELUM_CHECKOUT";
            AttendanceDataResponse attendanceData = new AttendanceDataResponse(
                    attendance.getId(),
                    attendance.getCheckInTime() != null ? attendance.getCheckInTime().format(dateTimeFormatter) : null,
                    attendance.getCheckOutTime() != null ? attendance.getCheckOutTime().format(dateTimeFormatter)
                            : null,
                    attendance.getAttendanceDate() != null
                            ? attendance.getAttendanceDate().toLocalDate().format(dateFormatter)
                            : null,
                    attendance.getCheckInStatus() == Attendance.CheckInStatus.TERLAMBAT,
                    attendance.getCheckInNotes(),
                    attendance.getCheckOutNotes(),
                    status);
            data.add(new StudentListResponse(
                    student.getId().intValue(),
                    student.getUser().getId().intValue(),
                    student.getUser().getName(),
                    student.getSin(),
                    student.getUser().getEmail(),
                    attendanceData));
        }

        data.sort((a, b) -> {
            return a.getNim().compareToIgnoreCase(b.getNim());
        });

        return new StudentListPageResponse(
                data,
                (int) attendancePage.getTotalElements(),
                attendancePage.getTotalPages(),
                page,
                size);
    }

    @Override
    public StudentDetailResponse getStudentDetail(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ApiException("Student not found", HttpStatus.NOT_FOUND));

        List<Attendance> attendanceList = attendanceRepository.findAllByStudent_Id(
                studentId, Sort.by(Sort.Direction.ASC, "attendanceDate"));

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<AttendanceDataResponse> attendanceDataList = new ArrayList<>();
        for (Attendance attendance : attendanceList) {
            String status = attendance.getCheckInStatus() != null ? attendance.getCheckInStatus().name()
                    : "BELUM_CHECKOUT";
            AttendanceDataResponse attendanceData = new AttendanceDataResponse(
                    attendance.getId(),
                    attendance.getCheckInTime() != null ? attendance.getCheckInTime().format(dateTimeFormatter) : null,
                    attendance.getCheckOutTime() != null ? attendance.getCheckOutTime().format(dateTimeFormatter)
                            : null,
                    attendance.getAttendanceDate() != null
                            ? attendance.getAttendanceDate().toLocalDate().format(dateFormatter)
                            : null,
                    attendance.getCheckInStatus() == Attendance.CheckInStatus.TERLAMBAT,
                    attendance.getCheckInNotes(),
                    attendance.getCheckOutNotes(),
                    status);
            attendanceDataList.add(attendanceData);
        }

        // Response: attendanceData berupa List
        return new StudentDetailResponse(
                student.getId().intValue(),
                student.getUser().getId().intValue(),
                student.getUser().getName(),
                student.getSin(),
                student.getUser().getEmail(),
                attendanceDataList);
    }
}
