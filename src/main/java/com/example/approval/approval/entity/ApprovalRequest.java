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

@Entity
@Table(name = "approval_request")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApprovalRequest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ApprovalStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Builder
    public ApprovalRequest(String title, String content, ApprovalStatus status, User createdBy) {
        this.title = title;
        this.content = content;
        this.status = status;
        this.createdBy = createdBy;
    }

    public void updateStatus(ApprovalStatus status) {
        this.status = status;
    }
}
