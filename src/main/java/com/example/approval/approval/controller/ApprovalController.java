package com.example.approval.approval.controller;

import com.example.approval.approval.dto.request.ApprovalApproveRequest;
import com.example.approval.approval.dto.request.ApprovalCreateRequest;
import com.example.approval.approval.dto.request.ApprovalRejectRequest;
import com.example.approval.approval.dto.response.ApprovalCreateResponse;
import com.example.approval.approval.dto.response.ApprovalDetailResponse;
import com.example.approval.approval.dto.response.ApprovalListResponse;
import com.example.approval.approval.dto.response.ApprovalProcessResponse;
import com.example.approval.approval.enumtype.ApprovalStatus;
import com.example.approval.approval.service.ApprovalService;
import com.example.approval.global.response.PageResponse;
import com.example.approval.global.security.CurrentUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/approvals")
public class ApprovalController {

    private final ApprovalService approvalService;

    @PostMapping
    public ApprovalCreateResponse create(
            @CurrentUser Long userId,
            @Valid @RequestBody ApprovalCreateRequest request
    ) {
        return approvalService.create(userId, request);
    }

    @GetMapping
    public PageResponse<ApprovalListResponse> getApprovals(
            @RequestParam(required = false) ApprovalStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return approvalService.getApprovals(status, page, size);
    }

    @GetMapping("/{id}")
    public ApprovalDetailResponse getApproval(@PathVariable Long id) {
        return approvalService.getApproval(id);
    }

    @PostMapping("/{id}/approve")
    public ApprovalProcessResponse approve(
            @CurrentUser Long userId,
            @PathVariable Long id,
            @Valid @RequestBody ApprovalApproveRequest request
    ) {
        return approvalService.approve(userId, id, request);
    }

    @PostMapping("/{id}/reject")
    public ApprovalProcessResponse reject(
            @CurrentUser Long userId,
            @PathVariable Long id,
            @Valid @RequestBody ApprovalRejectRequest request
    ) {
        return approvalService.reject(userId, id, request);
    }
}
