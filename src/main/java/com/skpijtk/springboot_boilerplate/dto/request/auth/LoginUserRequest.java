package com.skpijtk.springboot_boilerplate.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginUserRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}