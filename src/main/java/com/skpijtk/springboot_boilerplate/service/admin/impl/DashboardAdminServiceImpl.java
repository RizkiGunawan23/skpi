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
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import com.skpijtk.springboot_boilerplate.dto.request.admin.dashboard.StudentAttendanceListQuery;
import com.skpijtk.springboot_boilerplate.dto.request.admin.dashboard.StudentCheckinListQuery;
import com.skpijtk.springboot_boilerplate.dto.request.admin.dashboard.StudentListQuery;
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
        List<String> allowedSortBy = List.of("nim", "status");

        if (!allowedSortBy.contains(query.getSortBy()))
            throw new ApiException("Invalid sort by value", HttpStatusCode.valueOf(400));

        if (!query.getSortDir().equalsIgnoreCase("asc") && !query.getSortDir().equalsIgnoreCase("desc"))
            throw new ApiException("Invalid sort direction value", HttpStatusCode.valueOf(400));

        if (query.getPage() < 0 || query.getSize() <= 0)
            throw new ApiException("Page must >= 0 and size must > 0", HttpStatusCode.valueOf(400));

        final LocalDate start = (query.getStartdate() != null && !query.getStartdate().isBlank())
                ? LocalDate.parse(query.getStartdate())
                : null;
        final LocalDate end = (query.getEnddate() != null && !query.getEnddate().isBlank())
                ? LocalDate.parse(query.getEnddate())
                : null;
        if (start != null && end != null && start.isAfter(end)) {
            throw new ApiException("Start date must be before or equal to end date", HttpStatusCode.valueOf(400));
        }

        Pageable pageable = PageRequest.of(query.getPage(), query.getSize(), Sort.by(Sort.Direction.ASC, "id"));

        Page<Attendance> attendancePage;
        if (query.getStudent_name() != null && !query.getStudent_name().isBlank()) {
            if (start != null && end != null) {
                attendancePage = attendanceRepository
                        .findAllByStudent_User_NameIgnoreCaseContainingAndAttendanceDateBetween(
                                query.getStudent_name(), java.sql.Date.valueOf(start), java.sql.Date.valueOf(end),
                                pageable);
            } else if (start != null) {
                attendancePage = attendanceRepository
                        .findAllByStudent_User_NameIgnoreCaseContainingAndAttendanceDateBetween(
                                query.getStudent_name(), java.sql.Date.valueOf(start), java.sql.Date.valueOf(start),
                                pageable);
            } else {
                attendancePage = attendanceRepository.findAllByStudent_User_NameIgnoreCaseContaining(
                        query.getStudent_name(), pageable);
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
            throw new ApiException("Student data not found.", HttpStatusCode.valueOf(404));
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<StudentListResponse> data = new ArrayList<>();
        for (Attendance attendance : attendancePage.getContent()) {
            Student student = attendance.getStudent();
            String studentName = student.getUser().getName();
            // Filter nama mahasiswa (partial, case-insensitive)
            if (query.getStudent_name() != null && !query.getStudent_name().isBlank()) {
                if (!studentName.toLowerCase().contains(query.getStudent_name().toLowerCase())) {
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
                query.getPage(),
                query.getSize());
    }

    @Override
    public StudentListPageResponse getStudentAttendanceList(StudentAttendanceListQuery query) {
        if (query.getPage() < 0 || query.getSize() <= 0)
            throw new ApiException("Page must >= 0 and size must > 0", HttpStatusCode.valueOf(400));

        final LocalDate start = (query.getStartdate() != null && !query.getStartdate().isBlank())
                ? LocalDate.parse(query.getStartdate())
                : null;
        final LocalDate end = (query.getEnddate() != null && !query.getEnddate().isBlank())
                ? LocalDate.parse(query.getEnddate())
                : null;
        if (start != null && end != null && start.isAfter(end)) {
            throw new ApiException("Start date must be before or equal to end date", HttpStatusCode.valueOf(400));
        }

        Pageable pageable = PageRequest.of(query.getPage(), query.getSize(), Sort.by(Sort.Direction.ASC, "id"));

        Page<Attendance> attendancePage;
        if (query.getStudent_id() != null && !query.getStudent_id().isBlank()) {
            Long studentId = Long.valueOf(query.getStudent_id());
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
            throw new ApiException("Student data not found.", HttpStatusCode.valueOf(404));
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

        data.sort((a, b) -> {
            return a.getNim().compareToIgnoreCase(b.getNim());
        });

        return new StudentListPageResponse(
                data,
                (int) attendancePage.getTotalElements(),
                attendancePage.getTotalPages(),
                query.getPage(),
                query.getSize());
    }

    @Override
    public StudentListPageResponse getStudentList(StudentListQuery query) {
        if (query.getPage() < 0 || query.getSize() <= 0)
            throw new ApiException("Page must >= 0 and size must > 0", HttpStatusCode.valueOf(400));

        final LocalDate start = (query.getStartdate() != null && !query.getStartdate().isBlank())
                ? LocalDate.parse(query.getStartdate())
                : null;
        final LocalDate end = (query.getEnddate() != null && !query.getEnddate().isBlank())
                ? LocalDate.parse(query.getEnddate())
                : null;
        if (start != null && end != null && start.isAfter(end)) {
            throw new ApiException("Start date must be before or equal to end date", HttpStatusCode.valueOf(400));
        }

        Pageable pageable = PageRequest.of(query.getPage(), query.getSize(), Sort.by(Sort.Direction.ASC, "id"));

        Page<Attendance> attendancePage;
        if (query.getStudent_name() != null && !query.getStudent_name().isBlank()) {
            String studentName = String.valueOf(query.getStudent_name());
            if (start != null && end != null) {
                attendancePage = attendanceRepository
                        .findAllByStudent_User_NameIgnoreCaseContainingAndAttendanceDateBetween(
                                studentName, java.sql.Date.valueOf(start), java.sql.Date.valueOf(end), pageable);
            } else if (start != null) {
                attendancePage = attendanceRepository
                        .findAllByStudent_User_NameIgnoreCaseContainingAndAttendanceDateBetween(
                                studentName, java.sql.Date.valueOf(start), java.sql.Date.valueOf(start), pageable);
            } else {
                attendancePage = attendanceRepository.findAllByStudent_User_NameIgnoreCaseContaining(studentName,
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

        if (attendancePage.getContent().isEmpty()) {
            throw new ApiException("Student data not found.", HttpStatusCode.valueOf(404));
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

        data.sort((a, b) -> {
            return a.getNim().compareToIgnoreCase(b.getNim());
        });

        return new StudentListPageResponse(
                data,
                (int) attendancePage.getTotalElements(),
                attendancePage.getTotalPages(),
                query.getPage(),
                query.getSize());
    }
}
