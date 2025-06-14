package com.skpijtk.springboot_boilerplate.dto.response.student.attendance;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentCheckoutResponse {
    private int studentId;
    private String studentName;
    private String nim;
    private int attendanceId;
    private String checkinTime;
    private String checkoutTime;
    private String attendanceDate;
    private String notesCheckin;
    private String notesCheckout;
    private String statusCheckin;
}
