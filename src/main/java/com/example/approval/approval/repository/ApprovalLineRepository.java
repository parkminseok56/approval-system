package com.example.approval.approval.repository;

import com.example.approval.approval.entity.ApprovalLine;
import com.example.approval.approval.entity.ApprovalRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApprovalLineRepository extends JpaRepository<ApprovalLine, Long> {

    List<ApprovalLine> findAllByRequestAndDeleteYnFalseOrderByOrderNoAsc(ApprovalRequest request);

    Optional<ApprovalLine> findByRequestAndApproverIdAndDeleteYnFalse(ApprovalRequest request, Long approverId);
}
