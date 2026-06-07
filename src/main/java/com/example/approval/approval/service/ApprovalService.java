package com.example.approval.approval.service;

import com.example.approval.approval.dto.request.ApprovalApproveRequest;
import com.example.approval.approval.dto.request.ApprovalCreateRequest;
import com.example.approval.approval.dto.request.ApprovalRejectRequest;
import com.example.approval.approval.dto.response.ApprovalCreateResponse;
import com.example.approval.approval.dto.response.ApprovalDetailResponse;
import com.example.approval.approval.dto.response.ApprovalListResponse;
import com.example.approval.approval.dto.response.ApprovalProcessResponse;
import com.example.approval.approval.enumtype.ApprovalStatus;
import com.example.approval.global.response.PageResponse;

public interface ApprovalService {

    ApprovalCreateResponse create(Long userId, ApprovalCreateRequest request);

    PageResponse<ApprovalListResponse> getApprovals(ApprovalStatus status, int page, int size);

    ApprovalDetailResponse getApproval(Long id);

    ApprovalProcessResponse approve(Long userId, Long id, ApprovalApproveRequest request);

    ApprovalProcessResponse reject(Long userId, Long id, ApprovalRejectRequest request);
}
