package com.example.approval.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String name;
}
