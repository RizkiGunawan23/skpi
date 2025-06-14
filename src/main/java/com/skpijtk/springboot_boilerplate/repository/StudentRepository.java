package com.skpijtk.springboot_boilerplate.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.skpijtk.springboot_boilerplate.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {
    long countByAttendanceListIsEmpty();

    boolean existsBySin(String sin);

    Optional<Student> findByUserEmail(String email);
}
