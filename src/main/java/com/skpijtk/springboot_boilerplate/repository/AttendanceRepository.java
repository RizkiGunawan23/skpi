package com.skpijtk.springboot_boilerplate.repository;

import com.skpijtk.springboot_boilerplate.model.Attendance;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
        long countByCheckInStatus(Attendance.CheckInStatus status);

        Page<Attendance> findAllByStudent_User_NameIgnoreCaseContainingAndAttendanceDateBetween(
                        String name, java.sql.Date start, java.sql.Date end, Pageable pageable);

        Page<Attendance> findAllByStudent_User_NameIgnoreCaseContaining(String name, Pageable pageable);

        Page<Attendance> findAllByStudent_IdAndAttendanceDateBetween(
                        Long studentId, java.sql.Date start, java.sql.Date end, Pageable pageable);

        Page<Attendance> findAllByStudent_Id(Long studentId, Pageable pageable);

        Page<Attendance> findAllByAttendanceDateBetween(
                        java.sql.Date start, java.sql.Date end, Pageable pageable);

        List<Attendance> findAllByStudent_Id(Long studentId, Sort sort);

        List<Attendance> findAllByStudent_IdAndCheckOutTimeIsNullOrderByAttendanceDateAsc(Long studentId);
}