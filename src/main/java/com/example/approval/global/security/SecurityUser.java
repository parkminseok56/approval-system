package com.example.approval.global.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SecurityUser {

    private final Long userId;
    private final String username;
}
