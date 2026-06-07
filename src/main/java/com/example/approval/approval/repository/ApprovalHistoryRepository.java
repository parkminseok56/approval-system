package com.example.approval.approval.repository;

import com.example.approval.approval.entity.ApprovalHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApprovalHistoryRepository extends JpaRepository<ApprovalHistory, Long> {
}
