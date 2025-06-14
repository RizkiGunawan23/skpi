package com.skpijtk.springboot_boilerplate.dto.request.student.attendance;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StudentCheckoutRequest {
    @NotBlank(message = "notesCheckout cannot be blank")
    private String notesCheckout;
}
