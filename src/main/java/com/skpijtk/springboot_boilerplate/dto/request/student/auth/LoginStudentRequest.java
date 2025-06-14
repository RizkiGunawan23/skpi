package com.skpijtk.springboot_boilerplate.dto.request.student.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginStudentRequest {
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email must be valid")
    public String email;

    @NotBlank(message = "Password cannot be blank")
    public String password;
}
