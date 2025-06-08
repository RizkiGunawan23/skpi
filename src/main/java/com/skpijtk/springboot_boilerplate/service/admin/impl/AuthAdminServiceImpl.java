package com.skpijtk.springboot_boilerplate.service.admin.impl;

import com.skpijtk.springboot_boilerplate.dto.request.admin.auth.LoginAdminRequest;
import com.skpijtk.springboot_boilerplate.dto.request.admin.auth.SignupAdminRequest;
import com.skpijtk.springboot_boilerplate.dto.response.admin.auth.LoginAdminResponse;
import com.skpijtk.springboot_boilerplate.dto.response.admin.auth.SignupAdminResponse;
import com.skpijtk.springboot_boilerplate.exception.ApiException;
import com.skpijtk.springboot_boilerplate.model.User;
import com.skpijtk.springboot_boilerplate.model.User.Role;
import com.skpijtk.springboot_boilerplate.repository.UserRepository;
import com.skpijtk.springboot_boilerplate.security.JwtUtil;
import com.skpijtk.springboot_boilerplate.service.admin.AuthAdminService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthAdminServiceImpl implements AuthAdminService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    @Transactional
    public SignupAdminResponse signupAdmin(SignupAdminRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ApiException("Email already exists", HttpStatusCode.valueOf(400));
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.ADMIN);
        userRepository.save(user);
        return new SignupAdminResponse(user.getEmail());
    }

    @Override
    public LoginAdminResponse loginAdmin(LoginAdminRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApiException("Invalid email or password", HttpStatusCode.valueOf(401)));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ApiException("Invalid email or password", HttpStatusCode.valueOf(401));
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return new LoginAdminResponse(user.getId(), token, user.getName(), user.getRole().name());
    }
}