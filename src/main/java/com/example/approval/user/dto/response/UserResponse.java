package com.example.approval.user.dto.response;

import com.example.approval.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {

    private final Long id;
    private final String name;
    private final String department;

    public static UserResponse from(User user) {
        String departmentName = user.getDepartment() != null ? user.getDepartment().getName() : null;
        return new UserResponse(user.getId(), user.getName(), departmentName);
    }
}
