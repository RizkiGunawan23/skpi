package com.skpijtk.springboot_boilerplate.service.student.impl;

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
import com.skpijtk.springboot_boilerplate.dto.request.student.profile.StudentProfileQuery;
import com.skpijtk.springboot_boilerplate.dto.response.FieldErrorResponse;
import com.skpijtk.springboot_boilerplate.dto.response.student.profile.AttendanceDataResponse;
import com.skpijtk.springboot_boilerplate.dto.response.student.profile.StudentProfilePageResponse;
import com.skpijtk.springboot_boilerplate.dto.response.student.profile.StudentProfileResponse;
import com.skpijtk.springboot_boilerplate.exception.ApiException;
import com.skpijtk.springboot_boilerplate.model.Attendance;
import com.skpijtk.springboot_boilerplate.model.Student;
import com.skpijtk.springboot_boilerplate.repository.AttendanceRepository;
import com.skpijtk.springboot_boilerplate.repository.StudentRepository;
import com.skpijtk.springboot_boilerplate.service.student.ProfileStudentService;
import com.skpijtk.springboot_boilerplate.validation.StudentProfileValidator;

@Service
public class ProfileStudentServiceImpl implements ProfileStudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Override
    public StudentProfilePageResponse getStudentProfile(String email, StudentProfileQuery query) {
        List<FieldErrorResponse> errors = StudentProfileValidator.validate(query);
        if (!errors.isEmpty())
            throw new ApiException(errors);

        Student student = studentRepository.findByUserEmail(email)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ResponseMessage.STUDENT_NOT_FOUND));

        LocalDate start = null, end = null;
        if (query.getStartdate() != null && !query.getStartdate().isBlank())
            start = LocalDate.parse(query.getStartdate());
        if (query.getEnddate() != null && !query.getEnddate().isBlank())
            end = LocalDate.parse(query.getEnddate());

        int page = query.getPage() != null && !query.getPage().isBlank() ? Integer.parseInt(query.getPage()) : 0;
        int size = query.getSize() != null && !query.getSize().isBlank() ? Integer.parseInt(query.getSize()) : 10;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));

        Page<Attendance> attendancePage;
        if (start != null && end != null) {
            attendancePage = attendanceRepository.findAllByStudent_IdAndAttendanceDateBetween(
                    student.getId(), java.sql.Date.valueOf(start), java.sql.Date.valueOf(end), pageable);
        } else if (start != null) {
            attendancePage = attendanceRepository.findAllByStudent_IdAndAttendanceDateBetween(
                    student.getId(), java.sql.Date.valueOf(start), java.sql.Date.valueOf(start), pageable);
        } else {
            attendancePage = attendanceRepository.findAllByStudent_Id(student.getId(), pageable);
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<AttendanceDataResponse> attendanceDataList = new ArrayList<>();
        for (Attendance attendance : attendancePage.getContent()) {
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

        StudentProfileResponse studentProfile = new StudentProfileResponse(
                student.getId().intValue(),
                student.getUser().getName(),
                student.getSin(),
                attendanceDataList);

        return new StudentProfilePageResponse(
                studentProfile,
                (int) attendancePage.getTotalElements(),
                attendancePage.getTotalPages(),
                page,
                size);
    }
}
