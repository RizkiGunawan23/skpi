package com.skpijtk.springboot_boilerplate.dto.request.admin.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginAdminRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}