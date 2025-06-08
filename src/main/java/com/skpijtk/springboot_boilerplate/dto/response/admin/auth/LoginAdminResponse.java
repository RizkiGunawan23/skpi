package com.skpijtk.springboot_boilerplate.dto.response.admin.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginAdminResponse {
    private Long idUser;
    private String token;
    private String name;
    private String role;
}