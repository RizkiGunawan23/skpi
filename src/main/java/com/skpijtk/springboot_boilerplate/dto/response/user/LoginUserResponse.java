package com.skpijtk.springboot_boilerplate.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginUserResponse {
    private Long idUser;
    private String token;
    private String name;
    private String role;
}