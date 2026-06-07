package com.example.approval.approval.repository;

import com.example.approval.approval.entity.ApprovalRequest;
import com.example.approval.approval.enumtype.ApprovalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApprovalRequestRepository extends JpaRepository<ApprovalRequest, Long> {

    Page<ApprovalRequest> findAllByDeleteYnFalse(Pageable pageable);

    Page<ApprovalRequest> findAllByStatusAndDeleteYnFalse(ApprovalStatus status, Pageable pageable);

    Optional<ApprovalRequest> findByIdAndDeleteYnFalse(Long id);
}
