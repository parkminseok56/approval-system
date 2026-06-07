package com.example.approval.approval.entity;

import com.example.approval.approval.enumtype.ApprovalAction;
import com.example.approval.global.entity.BaseEntity;
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

@Entity
@Table(name = "approval_history")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApprovalHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private ApprovalRequest request;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ApprovalAction action;

    @Column(length = 500)
    private String comment;

    @Builder
    public ApprovalHistory(ApprovalRequest request, ApprovalAction action, String comment) {
        this.request = request;
        this.action = action;
        this.comment = comment;
    }
}
