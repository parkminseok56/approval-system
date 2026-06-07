package com.example.approval.approval.dto.response;

import com.example.approval.approval.enumtype.ApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApprovalCreateResponse {

    private final Long id;
    private final ApprovalStatus status;
    private final String message;
}
