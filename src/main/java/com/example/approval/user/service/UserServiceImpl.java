package com.example.approval.user.service;

import com.example.approval.user.dto.response.UserResponse;
import com.example.approval.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserResponse> getUsers() {
        return userRepository.findAllByDeleteYnFalse().stream()
                .map(UserResponse::from)
                .toList();
    }
}
