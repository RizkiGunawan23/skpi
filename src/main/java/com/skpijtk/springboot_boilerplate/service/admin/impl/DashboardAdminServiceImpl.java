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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import com.skpijtk.springboot_boilerplate.dto.request.admin.dashboard.StudentCheckinListQuery;
import com.skpijtk.springboot_boilerplate.dto.response.admin.common.AttendanceDataResponse;
import com.skpijtk.springboot_boilerplate.dto.response.admin.common.StudentAttendanceListPageResponse;
import com.skpijtk.springboot_boilerplate.dto.response.admin.common.StudentAttendanceListResponse;
import com.skpijtk.springboot_boilerplate.dto.response.admin.dashboard.CheckinResumeResponse;
import com.skpijtk.springboot_boilerplate.dto.response.admin.dashboard.StudentTotalResponse;
import com.skpijtk.springboot_boilerplate.exception.ApiException;
import com.skpijtk.springboot_boilerplate.model.Attendance;
import com.skpijtk.springboot_boilerplate.model.Student;
import com.skpijtk.springboot_boilerplate.repository.AttendanceRepository;
import com.skpijtk.springboot_boilerplate.repository.StudentRepository;
import com.skpijtk.springboot_boilerplate.service.admin.DashboardAdminService;

import jakarta.persistence.criteria.JoinType;

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
    public StudentAttendanceListPageResponse getStudentCheckinList(StudentCheckinListQuery query) {
        List<String> allowedSortBy = List.of("nim", "status");

        if (!allowedSortBy.contains(query.getSortBy()))
            throw new ApiException("Invalid sort by value", HttpStatusCode.valueOf(400));

        if (!query.getSortDir().equalsIgnoreCase("asc") && !query.getSortDir().equalsIgnoreCase("desc"))
            throw new ApiException("Invalid sort direction value", HttpStatusCode.valueOf(400));

        if (query.getPage() < 0 || query.getSize() <= 0)
            throw new ApiException("Page must >= 0 and size must > 0", HttpStatusCode.valueOf(400));

        final LocalDate start = (query.getStartDate() != null && !query.getStartDate().isBlank())
                ? LocalDate.parse(query.getStartDate())
                : null;
        final LocalDate end = (query.getEndDate() != null && !query.getEndDate().isBlank())
                ? LocalDate.parse(query.getEndDate())
                : null;
        if (start != null && end != null && start.isAfter(end)) {
            throw new ApiException("Start date must be before or equal to end date", HttpStatusCode.valueOf(400));
        }

        Specification<Student> spec = Specification.where(null);
        if (query.getStudent_name() != null && !query.getStudent_name().isBlank()) {
            spec = spec.and((root, cq, cb) -> cb.like(
                    cb.lower(root.get("user").get("name")),
                    "%" + query.getStudent_name().toLowerCase() + "%"));
        }
        if (start != null && end != null) {
            spec = spec.and((root, cq, cb) -> cb.between(
                    root.join("attendanceList", JoinType.LEFT).get("attendanceDate"),
                    java.sql.Date.valueOf(start),
                    java.sql.Date.valueOf(end)));
        } else if (start != null) {
            spec = spec.and((root, cq, cb) -> cb.equal(
                    root.join("attendanceList", JoinType.LEFT).get("attendanceDate"),
                    java.sql.Date.valueOf(start)));
        }

        Pageable pageable;
        if (query.getSortBy().equals("nim")) {
            pageable = PageRequest.of(query.getPage(), query.getSize(),
                    Sort.by(query.getSortDir().equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC,
                            "sin"));
        } else {
            // Jika sort by status, sorting di Java, paging tetap di database (by NIM ASC)
            pageable = PageRequest.of(query.getPage(), query.getSize(), Sort.by(Sort.Direction.ASC, "sin"));
        }

        Page<Student> studentPage = studentRepository.findAll(spec, pageable);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<StudentAttendanceListResponse> data = new ArrayList<>();
        for (Student student : studentPage.getContent()) {
            Attendance attendance = student.getAttendanceList().isEmpty() ? null : student.getAttendanceList().get(0);
            String status = null;
            if (attendance != null && attendance.getCheckInStatus() != null) {
                status = attendance.getCheckInStatus().name();
            } else {
                status = "BELUM_CHECKOUT";
            }
            AttendanceDataResponse attendanceData = attendance == null ? null
                    : new AttendanceDataResponse(
                            attendance.getId(),
                            attendance.getCheckInTime() != null ? attendance.getCheckInTime().format(dateTimeFormatter)
                                    : null,
                            attendance.getCheckOutTime() != null
                                    ? attendance.getCheckOutTime().format(dateTimeFormatter)
                                    : null,
                            attendance.getAttendanceDate() != null
                                    ? attendance.getAttendanceDate().toLocalDate().format(dateFormatter)
                                    : null,
                            attendance.getCheckInStatus() == Attendance.CheckInStatus.TERLAMBAT,
                            attendance.getCheckInNotes(),
                            attendance.getCheckOutNotes(),
                            status);
            data.add(new StudentAttendanceListResponse(
                    student.getId().intValue(),
                    student.getUser().getId().intValue(),
                    student.getUser().getName(),
                    student.getSin(),
                    student.getUser().getEmail(),
                    attendanceData));
        }

        if (query.getSortBy().equals("status")) {
            data.sort((a, b) -> {
                String s1 = a.getAttendanceData() != null ? a.getAttendanceData().getStatus() : "BELUM_CHECKOUT";
                String s2 = b.getAttendanceData() != null ? b.getAttendanceData().getStatus() : "BELUM_CHECKOUT";
                int p1 = getStatusPriority(s1);
                int p2 = getStatusPriority(s2);
                return Integer.compare(p1, p2);
            });
            if (query.getSortDir().equalsIgnoreCase("desc")) {
                java.util.Collections.reverse(data);
            }
        }

        return new StudentAttendanceListPageResponse(
                data,
                (int) studentPage.getTotalElements(),
                studentPage.getTotalPages(),
                query.getPage(),
                query.getSize());
    }
}
