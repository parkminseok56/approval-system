package com.example.approval.approval.dto.response;

import com.example.approval.approval.entity.ApprovalRequest;
import com.example.approval.approval.enumtype.ApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApprovalListResponse {

    private final Long id;
    private final String title;
    private final ApprovalStatus status;
    private final String createdBy;

    public static ApprovalListResponse from(ApprovalRequest request) {
        return new ApprovalListResponse(
                request.getId(),
                request.getTitle(),
                request.getStatus(),
                request.getCreatedBy().getName()
        );
    }
}
