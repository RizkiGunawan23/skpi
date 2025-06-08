package com.skpijtk.springboot_boilerplate.service.admin;

import com.skpijtk.springboot_boilerplate.dto.response.admin.profile.AdminProfileResponse;

public interface ProfileAdminService {
    AdminProfileResponse getAdminProfile(String email);
}
