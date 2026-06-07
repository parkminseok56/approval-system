package com.example.approval.department.dto.response;

import com.example.approval.department.entity.Department;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DepartmentResponse {

    private final Long id;
    private final String name;
    private final Long parentId;

    public static DepartmentResponse from(Department department) {
        return new DepartmentResponse(
                department.getId(),
                department.getName(),
                department.getParentId()
        );
    }
}
