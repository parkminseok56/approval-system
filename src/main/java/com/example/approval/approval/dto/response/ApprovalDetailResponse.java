package com.example.approval.approval.dto.response;

import com.example.approval.approval.entity.ApprovalLine;
import com.example.approval.approval.entity.ApprovalRequest;
import com.example.approval.approval.enumtype.ApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ApprovalDetailResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final ApprovalStatus status;
    private final List<ApprovalLineResponse> lines;

    public static ApprovalDetailResponse from(ApprovalRequest request, List<ApprovalLine> lines) {
        List<ApprovalLineResponse> lineResponses = lines.stream()
                .map(ApprovalLineResponse::from)
                .toList();

        return new ApprovalDetailResponse(
                request.getId(),
                request.getTitle(),
                request.getContent(),
                request.getStatus(),
                lineResponses
        );
    }

    @Getter
    @AllArgsConstructor
    public static class ApprovalLineResponse {
        private final int order;
        private final String approver;
        private final ApprovalStatus status;

        public static ApprovalLineResponse from(ApprovalLine line) {
            return new ApprovalLineResponse(
                    line.getOrderNo(),
                    line.getApprover().getName(),
                    line.getStatus()
            );
        }
    }
}
