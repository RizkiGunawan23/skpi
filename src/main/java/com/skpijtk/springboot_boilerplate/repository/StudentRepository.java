package com.skpijtk.springboot_boilerplate.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.skpijtk.springboot_boilerplate.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {
    long countByAttendanceListIsEmpty();

    @Override
    @EntityGraph(attributePaths = { "attendanceList", "user" })
    Page<Student> findAll(Specification<Student> spec, Pageable pageable);
}
