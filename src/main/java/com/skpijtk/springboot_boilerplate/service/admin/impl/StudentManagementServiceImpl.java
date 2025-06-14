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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skpijtk.springboot_boilerplate.constant.ResponseMessage;
import com.skpijtk.springboot_boilerplate.dto.request.admin.studentmanagement.CreateStudentRequest;
import com.skpijtk.springboot_boilerplate.dto.request.admin.studentmanagement.StudentListQuery;
import com.skpijtk.springboot_boilerplate.dto.request.admin.studentmanagement.UpdateStudentRequest;
import com.skpijtk.springboot_boilerplate.dto.response.FieldErrorResponse;
import com.skpijtk.springboot_boilerplate.dto.response.admin.common.AttendanceDataResponse;
import com.skpijtk.springboot_boilerplate.dto.response.admin.common.StudentListPageResponse;
import com.skpijtk.springboot_boilerplate.dto.response.admin.common.StudentListResponse;
import com.skpijtk.springboot_boilerplate.dto.response.admin.studentmanagement.DeleteStudentResponse;
import com.skpijtk.springboot_boilerplate.dto.response.admin.studentmanagement.StudentResponse;
import com.skpijtk.springboot_boilerplate.exception.ApiException;
import com.skpijtk.springboot_boilerplate.model.Attendance;
import com.skpijtk.springboot_boilerplate.model.Student;
import com.skpijtk.springboot_boilerplate.model.User;
import com.skpijtk.springboot_boilerplate.repository.AttendanceRepository;
import com.skpijtk.springboot_boilerplate.repository.StudentRepository;
import com.skpijtk.springboot_boilerplate.repository.UserRepository;
import com.skpijtk.springboot_boilerplate.service.admin.StudentManagementService;
import com.skpijtk.springboot_boilerplate.validation.CreateUpdateStudentValidator;
import com.skpijtk.springboot_boilerplate.validation.StudentListValidator;

@Service
public class StudentManagementServiceImpl implements StudentManagementService {
        @Autowired
        UserRepository userRepository;

        @Autowired
        StudentRepository studentRepository;

        @Autowired
        AttendanceRepository attendanceRepository;

        @Autowired
        private PasswordEncoder passwordEncoder;

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
                int page = query.getPage() != null && !query.getPage().isBlank() ? Integer.parseInt(query.getPage())
                                : 0;
                int size = query.getSize() != null && !query.getSize().isBlank() ? Integer.parseInt(query.getSize())
                                : 10;

                Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));

                Page<Attendance> attendancePage;
                if (studentNameQuery != null && !studentNameQuery.isBlank()) {
                        if (start != null && end != null) {
                                attendancePage = attendanceRepository
                                                .findAllByStudent_User_NameIgnoreCaseContainingAndAttendanceDateBetween(
                                                                studentNameQuery, java.sql.Date.valueOf(start),
                                                                java.sql.Date.valueOf(end), pageable);
                        } else if (start != null) {
                                attendancePage = attendanceRepository
                                                .findAllByStudent_User_NameIgnoreCaseContainingAndAttendanceDateBetween(
                                                                studentNameQuery, java.sql.Date.valueOf(start),
                                                                java.sql.Date.valueOf(start), pageable);
                        } else if (end != null) {
                                attendancePage = attendanceRepository
                                                .findAllByStudent_User_NameIgnoreCaseContainingAndAttendanceDateBetween(
                                                                studentNameQuery, java.sql.Date.valueOf(end),
                                                                java.sql.Date.valueOf(end), pageable);
                        } else {
                                attendancePage = attendanceRepository.findAllByStudent_User_NameIgnoreCaseContaining(
                                                studentNameQuery,
                                                pageable);
                        }
                } else if (start != null && end != null) {
                        attendancePage = attendanceRepository
                                        .findAllByAttendanceDateBetween(
                                                        java.sql.Date.valueOf(start), java.sql.Date.valueOf(end),
                                                        pageable);
                } else if (start != null) {
                        attendancePage = attendanceRepository
                                        .findAllByAttendanceDateBetween(
                                                        java.sql.Date.valueOf(start), java.sql.Date.valueOf(start),
                                                        pageable);
                } else if (end != null) {
                        attendancePage = attendanceRepository
                                        .findAllByAttendanceDateBetween(
                                                        java.sql.Date.valueOf(end), java.sql.Date.valueOf(end),
                                                        pageable);
                } else {
                        attendancePage = attendanceRepository.findAll(pageable);
                }

                if (attendancePage.getContent().isEmpty())
                        throw new ApiException(HttpStatus.NOT_FOUND, ResponseMessage.STUDENT_NOT_FOUND);

                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                List<StudentListResponse> data = new ArrayList<>();
                for (Attendance attendance : attendancePage.getContent()) {
                        Student student = attendance.getStudent();

                        String status = attendance.getCheckInStatus() != null ? attendance.getCheckInStatus().name()
                                        : "BELUM_CHECKOUT";
                        AttendanceDataResponse attendanceData = new AttendanceDataResponse(
                                        attendance.getId(),
                                        attendance.getCheckInTime() != null
                                                        ? attendance.getCheckInTime().format(dateTimeFormatter)
                                                        : null,
                                        attendance.getCheckOutTime() != null
                                                        ? attendance.getCheckOutTime().format(dateTimeFormatter)
                                                        : null,
                                        attendance.getAttendanceDate() != null
                                                        ? attendance.getAttendanceDate().toLocalDate()
                                                                        .format(dateFormatter)
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
        public StudentResponse getStudentDetail(Long studentId) {
                Student student = studentRepository.findById(studentId)
                                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                ResponseMessage.STUDENT_NOT_FOUND));

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
                                        attendance.getCheckInTime() != null
                                                        ? attendance.getCheckInTime().format(dateTimeFormatter)
                                                        : null,
                                        attendance.getCheckOutTime() != null
                                                        ? attendance.getCheckOutTime().format(dateTimeFormatter)
                                                        : null,
                                        attendance.getAttendanceDate() != null
                                                        ? attendance.getAttendanceDate().toLocalDate()
                                                                        .format(dateFormatter)
                                                        : null,
                                        attendance.getCheckInStatus() == Attendance.CheckInStatus.TERLAMBAT,
                                        attendance.getCheckInNotes(),
                                        attendance.getCheckOutNotes(),
                                        status);
                        attendanceDataList.add(attendanceData);
                }

                // Response: attendanceData berupa List
                return new StudentResponse(
                                student.getId().intValue(),
                                student.getUser().getId().intValue(),
                                student.getUser().getName(),
                                student.getSin(),
                                student.getUser().getEmail(),
                                attendanceDataList);
        }

        @Override
        @Transactional
        public StudentResponse createStudent(CreateStudentRequest request) {
                List<FieldErrorResponse> errors = CreateUpdateStudentValidator.validate(request, LocalDate.now());

                if (request.getNim() != null && studentRepository.existsBySin(request.getNim()))
                        errors.add(new FieldErrorResponse("nim", "NIM already exists"));

                if (request.getEmail() != null && userRepository.existsByEmail(request.getEmail()))
                        errors.add(new FieldErrorResponse("email", ResponseMessage.EMAIL_ALREADY_EXISTS));

                if (!errors.isEmpty())
                        throw new ApiException(errors);

                User user = new User();
                user.setName(request.getStudentName());
                user.setEmail(request.getEmail());
                user.setPassword(passwordEncoder.encode(request.getPassword()));
                user.setRole(User.Role.MAHASISWA);
                user = userRepository.save(user);

                Student student = new Student();
                student.setSin(request.getNim());
                student.setUser(user);
                student = studentRepository.save(student);

                return new StudentResponse(
                                student.getId().intValue(),
                                user.getId().intValue(),
                                user.getName(),
                                student.getSin(),
                                user.getEmail(),
                                new ArrayList<>());
        }

        @Override
        @Transactional
        public StudentResponse updateStudent(UpdateStudentRequest request, Long studentId) {
                Student student = studentRepository.findById(studentId)
                                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                ResponseMessage.STUDENT_NOT_FOUND));

                List<FieldErrorResponse> errors = CreateUpdateStudentValidator.validate(request, LocalDate.now());

                if (request.getNim() != null && !request.getNim().equals(student.getSin())
                                && studentRepository.existsBySin(request.getNim()))
                        errors.add(new FieldErrorResponse("nim", "NIM already exists"));

                if (request.getEmail() != null && !request.getEmail().equals(student.getUser().getEmail())
                                && userRepository.existsByEmail(request.getEmail()))
                        errors.add(new FieldErrorResponse("email", ResponseMessage.EMAIL_ALREADY_EXISTS));

                if (!errors.isEmpty())
                        throw new ApiException(errors);

                User user = student.getUser();
                user.setName(request.getStudentName());
                user.setEmail(request.getEmail());
                user = userRepository.save(user);

                student.setSin(request.getNim());
                student = studentRepository.save(student);

                return new StudentResponse(
                                student.getId().intValue(),
                                user.getId().intValue(),
                                user.getName(),
                                student.getSin(),
                                user.getEmail(),
                                new ArrayList<>());
        }

        @Override
        @Transactional
        public DeleteStudentResponse deleteStudent(Long studentId) {
                Student student = studentRepository.findById(studentId)
                                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                ResponseMessage.STUDENT_NOT_FOUND));

                String studentName = student.getUser().getName();
                studentRepository.delete(student);
                userRepository.delete(student.getUser());

                return new DeleteStudentResponse(
                                student.getId().intValue(),
                                studentName,
                                student.getSin());
        }
}
