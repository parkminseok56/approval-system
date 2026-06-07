package com.example.approval.department.service;

import com.example.approval.department.dto.response.DepartmentResponse;

import java.util.List;

public interface DepartmentService {

    List<DepartmentResponse> getDepartments();
}
