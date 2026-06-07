package com.example.approval.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class ApprovalNotificationService {

    private static final String PENDING_KEY_PREFIX = "approval:pending:";
    private static final Duration TTL = Duration.ofHours(24);

    private final RedisTemplate<String, Object> redisTemplate;

    public void notifyPendingApproval(Long approverId, Long approvalId) {
        String key = PENDING_KEY_PREFIX + approverId;
        redisTemplate.opsForList().rightPush(key, String.valueOf(approvalId));
        redisTemplate.expire(key, TTL);
    }

    public void removePendingApproval(Long approverId, Long approvalId) {
        String key = PENDING_KEY_PREFIX + approverId;
        redisTemplate.opsForList().remove(key, 1, String.valueOf(approvalId));
    }
}
