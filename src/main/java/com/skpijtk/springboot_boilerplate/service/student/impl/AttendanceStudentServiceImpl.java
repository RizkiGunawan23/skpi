package com.skpijtk.springboot_boilerplate.service.student.impl;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skpijtk.springboot_boilerplate.constant.ResponseMessage;
import com.skpijtk.springboot_boilerplate.dto.request.student.attendance.StudentCheckinRequest;
import com.skpijtk.springboot_boilerplate.dto.request.student.attendance.StudentCheckoutRequest;
import com.skpijtk.springboot_boilerplate.dto.response.student.attendance.StudentCheckinResponse;
import com.skpijtk.springboot_boilerplate.dto.response.student.attendance.StudentCheckoutResponse;
import com.skpijtk.springboot_boilerplate.exception.ApiException;
import com.skpijtk.springboot_boilerplate.model.AppSettings;
import com.skpijtk.springboot_boilerplate.model.Attendance;
import com.skpijtk.springboot_boilerplate.model.Student;
import com.skpijtk.springboot_boilerplate.repository.AppSettingsRepository;
import com.skpijtk.springboot_boilerplate.repository.AttendanceRepository;
import com.skpijtk.springboot_boilerplate.repository.StudentRepository;
import com.skpijtk.springboot_boilerplate.service.student.AttendanceStudentService;

@Service
public class AttendanceStudentServiceImpl implements AttendanceStudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private AppSettingsRepository appSettingsRepository;

    private final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    @Transactional
    public StudentCheckinResponse checkinStudent(String email, StudentCheckinRequest request) {
        Student student = studentRepository.findByUserEmail(email)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ResponseMessage.STUDENT_NOT_FOUND));

        LocalDate today = LocalDate.now();

        if (attendanceRepository.findAllByStudent_IdAndAttendanceDateBetween(
                student.getId(), java.sql.Date.valueOf(today), java.sql.Date.valueOf(today), null)
                .stream().findFirst().isPresent()) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    ResponseMessage.STUDENT_ALREADY_CHECKED_IN.format(student.getUser().getName()));
        }

        LocalDate yesterday = today.minusDays(1);
        Optional<Attendance> yesterdayAttendance = attendanceRepository
                .findAllByStudent_IdAndAttendanceDateBetween(student.getId(),
                        java.sql.Date.valueOf(yesterday), java.sql.Date.valueOf(yesterday), null)
                .stream().findFirst();

        if (yesterdayAttendance.isPresent() && yesterdayAttendance.get().getCheckOutTime() == null)
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    ResponseMessage.STUDENT_NOT_YET_CHECKED_OUT_YESTERDAY.format(student.getUser().getName()));

        AppSettings appSettings = appSettingsRepository.findById(1)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ResponseMessage.APP_SETTINGS_NOT_FOUND));
        LocalTime defaultCheckin = appSettings.getDefaultCheckInTime().toLocalTime();
        int lateTolerance = appSettings.getCheckInLateToleranceMinutes();

        LocalDateTime now = LocalDateTime.now();
        LocalTime checkinLimit = defaultCheckin.plusMinutes(lateTolerance);

        Attendance.CheckInStatus status = now.toLocalTime().isAfter(checkinLimit) ? Attendance.CheckInStatus.TERLAMBAT
                : Attendance.CheckInStatus.TEPAT_WAKTU;

        Attendance attendance = new Attendance();
        attendance.setStudent(student);
        attendance.setAttendanceDate(Date.valueOf(today));
        attendance.setCheckInTime(now);
        attendance.setCheckInStatus(status);
        attendance.setCheckInNotes(request.getNotesCheckin());
        attendance = attendanceRepository.save(attendance);

        return new StudentCheckinResponse(
                student.getId().intValue(),
                student.getUser().getName(),
                student.getSin(),
                attendance.getId().intValue(),
                attendance.getCheckInTime().format(DATE_TIME_FORMATTER),
                attendance.getCheckOutTime() != null ? attendance.getCheckOutTime().format(DATE_TIME_FORMATTER) : null,
                attendance.getAttendanceDate().toLocalDate().format(DATE_FORMATTER),
                attendance.getCheckInNotes(),
                attendance.getCheckOutNotes(),
                attendance.getCheckInStatus().name());
    }

    @Override
    @Transactional
    public StudentCheckoutResponse checkoutStudent(String email, StudentCheckoutRequest request) {
        Student student = studentRepository.findByUserEmail(email)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ResponseMessage.STUDENT_NOT_FOUND));

        // Cari attendance yang belum checkout, urutkan dari attendance_date paling lama
        Optional<Attendance> attendanceOpt = attendanceRepository
                .findAllByStudent_IdAndCheckOutTimeIsNullOrderByAttendanceDateAsc(student.getId())
                .stream().findFirst();

        Attendance attendance = attendanceOpt.orElseThrow(
                () -> new ApiException(HttpStatus.NOT_FOUND,
                        ResponseMessage.STUDENT_NOT_YET_CHECK_IN_TODAY.format(student.getUser().getName())));

        if (attendance.getCheckOutTime() != null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, ResponseMessage.STUDENT_ALREADY_CHECKED_OUT
                    .format(student.getUser().getName()));
        }

        LocalDateTime now = LocalDateTime.now();

        attendance.setCheckOutTime(now);
        attendance.setCheckOutNotes(request.getNotesCheckout());
        attendanceRepository.save(attendance);

        return new StudentCheckoutResponse(
                student.getId().intValue(),
                student.getUser().getName(),
                student.getSin(),
                attendance.getId().intValue(),
                attendance.getCheckInTime().format(DATE_TIME_FORMATTER),
                attendance.getCheckOutTime().format(DATE_TIME_FORMATTER),
                attendance.getAttendanceDate().toLocalDate().format(DATE_FORMATTER),
                attendance.getCheckInNotes(),
                attendance.getCheckOutNotes(),
                attendance.getCheckInStatus().name());
    }

}
