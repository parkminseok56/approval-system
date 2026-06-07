package com.example.approval.department.service;

import com.example.approval.department.dto.response.DepartmentResponse;
import com.example.approval.department.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    public List<DepartmentResponse> getDepartments() {
        return departmentRepository.findAllByDeleteYnFalse().stream()
                .map(DepartmentResponse::from)
                .toList();
    }
}
