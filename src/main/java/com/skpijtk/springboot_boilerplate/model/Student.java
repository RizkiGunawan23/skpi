package com.skpijtk.springboot_boilerplate.model;

import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "students")
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nim", nullable = false, unique = true)
    private String sin; // Student Identification Number

    @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", nullable = false, updatable = false, insertable = false)
    private Timestamp updatedAt;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @OneToMany(mappedBy = "student")
    private List<Attendance> attendanceList;
}
