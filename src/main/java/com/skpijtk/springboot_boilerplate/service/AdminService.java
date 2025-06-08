package com.skpijtk.springboot_boilerplate.service;

import com.skpijtk.springboot_boilerplate.dto.response.admin.AdminProfileResponse;

public interface AdminService {
    AdminProfileResponse getProfile(String email);
}
