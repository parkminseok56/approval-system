package com.example.approval.auth.service;

import com.example.approval.auth.dto.request.LoginRequest;
import com.example.approval.auth.dto.request.SignupRequest;
import com.example.approval.auth.dto.response.LoginResponse;
import com.example.approval.auth.dto.response.MeResponse;
import com.example.approval.global.exception.BusinessException;
import com.example.approval.global.exception.ErrorCode;
import com.example.approval.global.util.JwtUtil;
import com.example.approval.user.entity.User;
import com.example.approval.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public void signup(SignupRequest request) {
        if (userRepository.existsByUsernameAndDeleteYnFalse(request.getUsername())) {
            throw new BusinessException(ErrorCode.DUPLICATE_USERNAME);
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .build();

        userRepository.save(user);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsernameAndDeleteYnFalse(request.getUsername())
                .filter(found -> passwordEncoder.matches(request.getPassword(), found.getPassword()))
                .orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED));

        String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId(), user.getUsername());

        return new LoginResponse(accessToken, refreshToken);
    }

    @Override
    @Transactional(readOnly = true)
    public MeResponse getMe(Long userId) {
        User user = userRepository.findById(userId)
                .filter(found -> !found.isDeleteYn())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        return MeResponse.from(user);
    }
}
