package com.example.approval.user.service;

import com.example.approval.user.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    List<UserResponse> getUsers();
}
