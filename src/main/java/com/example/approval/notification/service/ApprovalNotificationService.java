package com.example.approval.notification.service;

public interface ApprovalNotificationService {

    void notifyPendingApproval(Long approverId, Long approvalId);

    void removePendingApproval(Long approverId, Long approvalId);
}
