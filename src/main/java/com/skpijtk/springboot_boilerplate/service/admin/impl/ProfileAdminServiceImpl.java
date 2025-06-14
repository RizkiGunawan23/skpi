package com.skpijtk.springboot_boilerplate.service.admin.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.skpijtk.springboot_boilerplate.dto.response.admin.profile.AdminProfileResponse;
import com.skpijtk.springboot_boilerplate.exception.ApiException;
import com.skpijtk.springboot_boilerplate.model.User;
import com.skpijtk.springboot_boilerplate.repository.UserRepository;
import com.skpijtk.springboot_boilerplate.service.admin.ProfileAdminService;

@Service
public class ProfileAdminServiceImpl implements ProfileAdminService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public AdminProfileResponse getAdminProfile(String email) {
        Optional<User> user = userRepository.findByEmail(email);

        if (!user.isPresent()) {
            throw new ApiException("User not found with email: " + email, HttpStatus.NOT_FOUND);
        }

        User foundUser = user.get();
        return new AdminProfileResponse(foundUser.getName(), foundUser.getRole().name());
    }
}
