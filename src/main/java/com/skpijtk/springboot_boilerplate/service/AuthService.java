package com.skpijtk.springboot_boilerplate.service;

import com.skpijtk.springboot_boilerplate.dto.request.user.SignupAdminRequest;
import com.skpijtk.springboot_boilerplate.dto.response.user.LoginUserResponse;
import com.skpijtk.springboot_boilerplate.dto.response.user.SignupAdminResponse;

import com.skpijtk.springboot_boilerplate.dto.request.user.LoginUserRequest;

public interface AuthService {
    SignupAdminResponse signupAdmin(SignupAdminRequest request);

    LoginUserResponse loginAdmin(LoginUserRequest request);
}