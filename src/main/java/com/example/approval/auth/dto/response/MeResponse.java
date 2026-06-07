package com.example.approval.auth.dto.response;

import com.example.approval.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MeResponse {

    private final Long id;
    private final String username;
    private final String name;
    private final String department;

    public static MeResponse from(User user) {
        String departmentName = user.getDepartment() != null ? user.getDepartment().getName() : null;
        return new MeResponse(user.getId(), user.getUsername(), user.getName(), departmentName);
    }
}
