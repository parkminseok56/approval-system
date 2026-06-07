package com.example.approval.department.repository;

import com.example.approval.department.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    List<Department> findAllByDeleteYnFalse();
}
