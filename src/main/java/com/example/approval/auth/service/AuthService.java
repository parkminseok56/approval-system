package com.example.approval.auth.service;

import com.example.approval.auth.dto.request.LoginRequest;
import com.example.approval.auth.dto.request.SignupRequest;
import com.example.approval.auth.dto.response.LoginResponse;
import com.example.approval.auth.dto.response.MeResponse;

public interface AuthService {

    void signup(SignupRequest request);

    LoginResponse login(LoginRequest request);

    MeResponse getMe(Long userId);
}
