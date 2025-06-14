package com.skpijtk.springboot_boilerplate.dto.response.student.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginStudentResponse {
    private Long idUser;
    private String token;
    private String name;
    private String role;
}
