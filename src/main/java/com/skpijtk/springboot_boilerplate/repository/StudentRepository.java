package com.skpijtk.springboot_boilerplate.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skpijtk.springboot_boilerplate.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
    long countByAttendanceListIsEmpty();
}
