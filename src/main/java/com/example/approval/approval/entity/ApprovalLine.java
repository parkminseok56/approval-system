package com.example.approval.approval.entity;

import com.example.approval.approval.enumtype.ApprovalStatus;
import com.example.approval.global.entity.BaseEntity;
import com.example.approval.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "approval_line")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApprovalLine extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private ApprovalRequest request;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_id", nullable = false)
    private User approver;

    @Column(name = "order_no", nullable = false)
    private int orderNo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ApprovalStatus status;

    @Column(length = 500)
    private String comment;

    @Column(name = "decision_at")
    private LocalDateTime decisionAt;

    @Builder
    public ApprovalLine(ApprovalRequest request, User approver, int orderNo, ApprovalStatus status) {
        this.request = request;
        this.approver = approver;
        this.orderNo = orderNo;
        this.status = status;
    }

    public void approve(String comment) {
        this.status = ApprovalStatus.APPROVED;
        this.comment = comment;
        this.decisionAt = LocalDateTime.now();
    }

    public void reject(String comment) {
        this.status = ApprovalStatus.REJECTED;
        this.comment = comment;
        this.decisionAt = LocalDateTime.now();
    }
}
