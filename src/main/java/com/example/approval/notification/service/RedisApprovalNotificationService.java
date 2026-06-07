package com.example.approval.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@Profile({"local", "prod"})
@RequiredArgsConstructor
public class RedisApprovalNotificationService implements ApprovalNotificationService {

    private static final String PENDING_KEY_PREFIX = "approval:pending:";
    private static final Duration TTL = Duration.ofHours(24);

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void notifyPendingApproval(Long approverId, Long approvalId) {
        String key = PENDING_KEY_PREFIX + approverId;
        redisTemplate.opsForList().rightPush(key, String.valueOf(approvalId));
        redisTemplate.expire(key, TTL);
    }

    @Override
    public void removePendingApproval(Long approverId, Long approvalId) {
        String key = PENDING_KEY_PREFIX + approverId;
        redisTemplate.opsForList().remove(key, 1, String.valueOf(approvalId));
    }
}
