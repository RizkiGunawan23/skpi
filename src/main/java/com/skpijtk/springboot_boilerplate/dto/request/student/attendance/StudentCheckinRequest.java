package com.skpijtk.springboot_boilerplate.dto.request.student.attendance;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StudentCheckinRequest {
    @NotBlank(message = "notesCheckin cannot be blank")
    private String notesCheckin;
}
