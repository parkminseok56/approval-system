package com.example.approval.notification.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile({"standalone", "test"})
public class NoOpApprovalNotificationService implements ApprovalNotificationService {

    @Override
    public void notifyPendingApproval(Long approverId, Long approvalId) {
    }

    @Override
    public void removePendingApproval(Long approverId, Long approvalId) {
    }
}
