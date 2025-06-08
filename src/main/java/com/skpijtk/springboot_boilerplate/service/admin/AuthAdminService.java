package com.skpijtk.springboot_boilerplate.service.admin;

import com.skpijtk.springboot_boilerplate.dto.request.admin.auth.LoginAdminRequest;
import com.skpijtk.springboot_boilerplate.dto.request.admin.auth.SignupAdminRequest;
import com.skpijtk.springboot_boilerplate.dto.response.admin.auth.LoginAdminResponse;
import com.skpijtk.springboot_boilerplate.dto.response.admin.auth.SignupAdminResponse;

public interface AuthAdminService {
    SignupAdminResponse signupAdmin(SignupAdminRequest request);

    LoginAdminResponse loginAdmin(LoginAdminRequest request);
}