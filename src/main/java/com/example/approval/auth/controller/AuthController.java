package com.example.approval.auth.controller;

import com.example.approval.auth.dto.request.LoginRequest;
import com.example.approval.auth.dto.request.SignupRequest;
import com.example.approval.auth.dto.response.LoginResponse;
import com.example.approval.auth.dto.response.MeResponse;
import com.example.approval.auth.service.AuthService;
import com.example.approval.global.response.ApiResponse;
import com.example.approval.global.security.CurrentUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ApiResponse<Void> signup(@Valid @RequestBody SignupRequest request) {
        authService.signup(request);
        return ApiResponse.success("SIGNUP_SUCCESS");
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/me")
    public MeResponse getMe(@CurrentUser Long userId) {
        return authService.getMe(userId);
    }
}
