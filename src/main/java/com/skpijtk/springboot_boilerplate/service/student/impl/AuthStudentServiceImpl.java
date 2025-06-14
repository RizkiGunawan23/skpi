package com.skpijtk.springboot_boilerplate.service.student.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.skpijtk.springboot_boilerplate.constant.ResponseMessage;
import com.skpijtk.springboot_boilerplate.dto.request.student.auth.LoginStudentRequest;
import com.skpijtk.springboot_boilerplate.dto.response.student.auth.LoginStudentResponse;
import com.skpijtk.springboot_boilerplate.exception.ApiException;
import com.skpijtk.springboot_boilerplate.model.User;
import com.skpijtk.springboot_boilerplate.repository.UserRepository;
import com.skpijtk.springboot_boilerplate.security.JwtUtil;
import com.skpijtk.springboot_boilerplate.service.student.AuthStudentService;

@Service
public class AuthStudentServiceImpl implements AuthStudentService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public LoginStudentResponse loginStudent(LoginStudentRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, ResponseMessage.INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new ApiException(HttpStatus.UNAUTHORIZED, ResponseMessage.INVALID_CREDENTIALS);

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return new LoginStudentResponse(user.getId(), token, user.getName(), user.getRole().name());
    }
}
