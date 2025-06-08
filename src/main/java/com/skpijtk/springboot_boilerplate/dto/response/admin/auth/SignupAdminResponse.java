package com.skpijtk.springboot_boilerplate.dto.response.admin.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignupAdminResponse {
    private String email;
}