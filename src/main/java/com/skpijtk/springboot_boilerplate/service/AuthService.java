package com.skpijtk.springboot_boilerplate.service;

import com.skpijtk.springboot_boilerplate.dto.request.auth.LoginUserRequest;
import com.skpijtk.springboot_boilerplate.dto.request.auth.SignupAdminRequest;
import com.skpijtk.springboot_boilerplate.dto.response.auth.LoginUserResponse;
import com.skpijtk.springboot_boilerplate.dto.response.auth.SignupAdminResponse;

public interface AuthService {
    SignupAdminResponse signupAdmin(SignupAdminRequest request);

    LoginUserResponse loginAdmin(LoginUserRequest request);
}