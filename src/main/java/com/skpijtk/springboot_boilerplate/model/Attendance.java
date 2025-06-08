package com.skpijtk.springboot_boilerplate.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "attendance", uniqueConstraints = @UniqueConstraint(columnNames = { "student_id", "attendance_date" }))
@AllArgsConstructor
@NoArgsConstructor
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "attendance_date", nullable = false)
    private Date attendanceDate;

    @Column(name = "check_in_time")
    private LocalDateTime checkInTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "check_in_status")
    private CheckInStatus checkInStatus;

    @Column(name = "check_in_notes", columnDefinition = "TEXT")
    private String checkInNotes;

    @Column(name = "check_out_time")
    private LocalDateTime checkOutTime;

    @Column(name = "check_out_notes", columnDefinition = "TEXT")
    private String checkOutNotes;

    @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

    public enum CheckInStatus {
        TEPAT_WAKTU,
        TERLAMBAT
    }

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
}