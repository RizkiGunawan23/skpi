package com.skpijtk.springboot_boilerplate.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skpijtk.springboot_boilerplate.constant.ResponseMessage;
import com.skpijtk.springboot_boilerplate.model.User;
import com.skpijtk.springboot_boilerplate.repository.UserRepository;

import io.jsonwebtoken.Claims;

import java.io.IOException;
import java.util.Collections;

public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    private void writeErrorResponse(int statusCode, HttpServletResponse response, String message) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(
                new java.util.HashMap<String, Object>() {
                    {
                        put("data", null);
                        put("message", message);
                    }
                });
        response.getWriter().write(json);
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();

        if (path.equals("/api/v1/admin/signup") || path.equals("/api/v1/admin/login")
                || path.equals("/api/v1/mahasiswa/login") || path.startsWith("/ws")) {
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            writeErrorResponse(HttpServletResponse.SC_UNAUTHORIZED, response,
                    "Authorization header missing or invalid");
            return;
        }

        String token = header.substring(7);
        try {
            if (!jwtUtil.validateToken(token)) {
                writeErrorResponse(HttpServletResponse.SC_UNAUTHORIZED, response, "Invalid token");
                return;
            }

            Claims claims = jwtUtil.getClaimsFromToken(token);
            String email = claims.getSubject();
            String role = claims.get("role", String.class);

            User user = userRepository.findByEmail(email).orElse(null);
            if (user == null) {
                writeErrorResponse(HttpServletResponse.SC_UNAUTHORIZED, response,
                        "User not found");
                return;
            }

            UserDetails userDetails = org.springframework.security.core.userdetails.User
                    .withUsername(user.getEmail())
                    .password(user.getPassword())
                    .roles(user.getRole().name())
                    .build();

            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, Collections.singleton(authority));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            writeErrorResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response,
                    ResponseMessage.INTERNAL_SERVER_ERROR.toString());
            return;
        }
        filterChain.doFilter(request, response);
    }
}