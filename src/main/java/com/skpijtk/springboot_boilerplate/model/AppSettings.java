package com.skpijtk.springboot_boilerplate.model;

import java.sql.Time;
import java.sql.Timestamp;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@Table(name = "appsettings")
@NoArgsConstructor
@AllArgsConstructor
public class AppSettings {
    @Id
    private Integer id = 1;

    @Column(name = "default_check_in_time", nullable = false)
    private Time defaultCheckInTime;

    @Column(name = "default_check_out_time", nullable = false)
    private Time defaultCheckOutTime;

    @Column(name = "check_in_late_tolerance_minutes", nullable = false)
    private Integer checkInLateToleranceMinutes;

    @Column(name = "check_out_late_tolerance_minutes", nullable = false)
    private Integer checkOutLateToleranceMinutes;

    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private Timestamp updatedAt;
}