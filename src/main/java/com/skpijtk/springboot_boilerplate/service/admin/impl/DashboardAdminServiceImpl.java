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

import com.skpijtk.springboot_boilerplate.constant.ResponseMessage;
import com.skpijtk.springboot_boilerplate.dto.request.admin.dashboard.StudentAttendanceListQuery;
import com.skpijtk.springboot_boilerplate.dto.request.admin.dashboard.StudentCheckinListQuery;
import com.skpijtk.springboot_boilerplate.dto.response.FieldErrorResponse;
import com.skpijtk.springboot_boilerplate.dto.response.admin.common.AttendanceDataResponse;
import com.skpijtk.springboot_boilerplate.dto.response.admin.common.StudentListPageResponse;
import com.skpijtk.springboot_boilerplate.dto.response.admin.common.StudentListResponse;
import com.skpijtk.springboot_boilerplate.dto.response.admin.dashboard.CheckinResumeResponse;
import com.skpijtk.springboot_boilerplate.dto.response.admin.dashboard.StudentTotalResponse;
import com.skpijtk.springboot_boilerplate.exception.ApiException;
import com.skpijtk.springboot_boilerplate.model.Attendance;
import com.skpijtk.springboot_boilerplate.model.Student;
import com.skpijtk.springboot_boilerplate.repository.AttendanceRepository;
import com.skpijtk.springboot_boilerplate.repository.StudentRepository;
import com.skpijtk.springboot_boilerplate.service.admin.DashboardAdminService;
import com.skpijtk.springboot_boilerplate.validation.StudentAttendanceListValidator;
import com.skpijtk.springboot_boilerplate.validation.StudentCheckinListValidator;

@Service
public class DashboardAdminServiceImpl implements DashboardAdminService {
    @Autowired
    StudentRepository studentRepository;

    @Autowired
    AttendanceRepository attendanceRepository;

    private int getStatusPriority(String status) {
        switch (status.toLowerCase()) {
            case "TEPAT_WAKTU":
                return 1;
            case "TERLAMBAT":
                return 2;
            case "BELUM_CHECKOUT":
                return 3;
            default:
                return 4;
        }
    }

    @Override
    public StudentTotalResponse getStudentTotal() {
        int studentTotal = (int) studentRepository.count();
        return new StudentTotalResponse(studentTotal);
    }

    @Override
    public CheckinResumeResponse getCheckinResume() {
        int studentTotal = (int) studentRepository.count();
        int lateCheckinTotal = (int) attendanceRepository.countByCheckInStatus(Attendance.CheckInStatus.TERLAMBAT);
        int checkinTotal = (int) attendanceRepository.count() + lateCheckinTotal;
        int notCheckinTotal = (int) studentRepository.countByAttendanceListIsEmpty();

        return new CheckinResumeResponse(studentTotal, checkinTotal, notCheckinTotal, lateCheckinTotal);
    }

    @Override
    public StudentListPageResponse getStudentCheckinList(StudentCheckinListQuery query) {
        List<FieldErrorResponse> errors = StudentCheckinListValidator.validate(query);
        if (!errors.isEmpty()) {
            throw new ApiException(errors);
        }

        String studentNameQuery = null;
        if (query.getStudent_name() != null && !query.getStudent_name().isBlank()) {
            studentNameQuery = query.getStudent_name();
        }
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
                                studentNameQuery, java.sql.Date.valueOf(start), java.sql.Date.valueOf(end),
                                pageable);
            } else if (start != null) {
                attendancePage = attendanceRepository
                        .findAllByStudent_User_NameIgnoreCaseContainingAndAttendanceDateBetween(
                                studentNameQuery, java.sql.Date.valueOf(start), java.sql.Date.valueOf(start),
                                pageable);
            } else {
                attendancePage = attendanceRepository.findAllByStudent_User_NameIgnoreCaseContaining(
                        studentNameQuery, pageable);
            }
        } else if (start != null && end != null) {
            attendancePage = attendanceRepository.findAllByAttendanceDateBetween(
                    java.sql.Date.valueOf(start), java.sql.Date.valueOf(end), pageable);
        } else if (start != null) {
            attendancePage = attendanceRepository.findAllByAttendanceDateBetween(
                    java.sql.Date.valueOf(start), java.sql.Date.valueOf(start), pageable);
        } else {
            attendancePage = attendanceRepository.findAll(pageable);
        }

        if (attendancePage.getContent().isEmpty()) {
            throw new ApiException(HttpStatus.NOT_FOUND, ResponseMessage.STUDENT_NOT_FOUND);
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<StudentListResponse> data = new ArrayList<>();
        for (Attendance attendance : attendancePage.getContent()) {
            Student student = attendance.getStudent();
            String studentName = student.getUser().getName();
            // Filter nama mahasiswa (partial, case-insensitive)
            if (studentNameQuery != null && !studentNameQuery.isBlank()) {
                if (!studentName.toLowerCase().contains(studentNameQuery.toLowerCase())) {
                    continue;
                }
            }
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
                    studentName,
                    student.getSin(),
                    student.getUser().getEmail(),
                    attendanceData));
        }

        if ("nim".equalsIgnoreCase(query.getSortBy())) {
            if ("desc".equalsIgnoreCase(query.getSortDir())) {
                data.sort((a, b) -> b.getNim().compareToIgnoreCase(a.getNim()));
            } else {
                data.sort((a, b) -> a.getNim().compareToIgnoreCase(b.getNim()));
            }
        } else if ("status".equalsIgnoreCase(query.getSortBy())) {
            data.sort((a, b) -> {
                String s1 = a.getAttendanceData() != null ? a.getAttendanceData().getStatus() : "BELUM_CHECKOUT";
                String s2 = b.getAttendanceData() != null ? b.getAttendanceData().getStatus() : "BELUM_CHECKOUT";
                int p1 = getStatusPriority(s1);
                int p2 = getStatusPriority(s2);
                return Integer.compare(p1, p2);
            });
            if ("desc".equalsIgnoreCase(query.getSortDir())) {
                java.util.Collections.reverse(data);
            }
        }

        return new StudentListPageResponse(
                data,
                (int) attendancePage.getTotalElements(),
                attendancePage.getTotalPages(),
                page,
                size);
    }

    @Override
    public StudentListPageResponse getStudentAttendanceList(StudentAttendanceListQuery query) {
        List<FieldErrorResponse> errors = StudentAttendanceListValidator.validate(query);
        if (!errors.isEmpty()) {
            throw new ApiException(errors);
        }

        Long studentId = null;
        if (query.getStudent_id() != null && !query.getStudent_id().isBlank()) {
            studentId = Long.valueOf(query.getStudent_id());
        }
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
        if (studentId != null) {
            if (start != null && end != null) {
                attendancePage = attendanceRepository.findAllByStudent_IdAndAttendanceDateBetween(
                        studentId, java.sql.Date.valueOf(start), java.sql.Date.valueOf(end), pageable);
            } else if (start != null) {
                attendancePage = attendanceRepository.findAllByStudent_IdAndAttendanceDateBetween(
                        studentId, java.sql.Date.valueOf(start), java.sql.Date.valueOf(start), pageable);
            } else {
                attendancePage = attendanceRepository.findAllByStudent_Id(studentId, pageable);
            }
        } else if (start != null && end != null) {
            attendancePage = attendanceRepository.findAllByAttendanceDateBetween(
                    java.sql.Date.valueOf(start), java.sql.Date.valueOf(end), pageable);
        } else if (start != null) {
            attendancePage = attendanceRepository.findAllByAttendanceDateBetween(
                    java.sql.Date.valueOf(start), java.sql.Date.valueOf(start), pageable);
        } else {
            attendancePage = attendanceRepository.findAll(pageable);
        }

        if (attendancePage.getContent().isEmpty()) {
            throw new ApiException(HttpStatus.NOT_FOUND, ResponseMessage.STUDENT_NOT_FOUND);
        }

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

        data.sort((a, b) -> a.getNim().compareToIgnoreCase(b.getNim()));

        return new StudentListPageResponse(
                data,
                (int) attendancePage.getTotalElements(),
                attendancePage.getTotalPages(),
                page,
                size);
    }
}
