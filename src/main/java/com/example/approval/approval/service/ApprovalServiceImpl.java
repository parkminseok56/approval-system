package com.example.approval.approval.service;

import com.example.approval.approval.dto.request.ApprovalApproveRequest;
import com.example.approval.approval.dto.request.ApprovalCreateRequest;
import com.example.approval.approval.dto.request.ApprovalRejectRequest;
import com.example.approval.approval.dto.response.ApprovalCreateResponse;
import com.example.approval.approval.dto.response.ApprovalDetailResponse;
import com.example.approval.approval.dto.response.ApprovalListResponse;
import com.example.approval.approval.dto.response.ApprovalProcessResponse;
import com.example.approval.approval.entity.ApprovalHistory;
import com.example.approval.approval.entity.ApprovalLine;
import com.example.approval.approval.entity.ApprovalRequest;
import com.example.approval.approval.enumtype.ApprovalAction;
import com.example.approval.approval.enumtype.ApprovalStatus;
import com.example.approval.approval.repository.ApprovalHistoryRepository;
import com.example.approval.approval.repository.ApprovalLineRepository;
import com.example.approval.approval.repository.ApprovalRequestRepository;
import com.example.approval.global.exception.BusinessException;
import com.example.approval.global.exception.ErrorCode;
import com.example.approval.global.response.PageResponse;
import com.example.approval.notification.service.ApprovalNotificationService;
import com.example.approval.user.entity.User;
import com.example.approval.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class ApprovalServiceImpl implements ApprovalService {

    private final ApprovalRequestRepository approvalRequestRepository;
    private final ApprovalLineRepository approvalLineRepository;
    private final ApprovalHistoryRepository approvalHistoryRepository;
    private final UserRepository userRepository;
    private final ApprovalNotificationService approvalNotificationService;

    @Override
    public ApprovalCreateResponse create(Long userId, ApprovalCreateRequest request) {
        User creator = getUser(userId);
        List<User> approvers = resolveApprovers(userId, request.getApproverIds());

        ApprovalRequest approvalRequest = ApprovalRequest.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .status(ApprovalStatus.PENDING)
                .createdBy(creator)
                .build();
        approvalRequestRepository.save(approvalRequest);

        for (int i = 0; i < approvers.size(); i++) {
            approvalLineRepository.save(ApprovalLine.builder()
                    .request(approvalRequest)
                    .approver(approvers.get(i))
                    .orderNo(i + 1)
                    .status(ApprovalStatus.PENDING)
                    .build());
        }

        approvalNotificationService.notifyPendingApproval(
                approvers.get(0).getId(),
                approvalRequest.getId()
        );

        return new ApprovalCreateResponse(
                approvalRequest.getId(),
                ApprovalStatus.PENDING,
                "APPROVAL_CREATED"
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ApprovalListResponse> getApprovals(ApprovalStatus status, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ApprovalRequest> result = status == null
                ? approvalRequestRepository.findAllByDeleteYnFalse(pageRequest)
                : approvalRequestRepository.findAllByStatusAndDeleteYnFalse(status, pageRequest);

        return new PageResponse<>(
                result.getContent().stream().map(ApprovalListResponse::from).toList(),
                page,
                size
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ApprovalDetailResponse getApproval(Long id) {
        ApprovalRequest request = getApprovalRequest(id);

        return ApprovalDetailResponse.from(
                request,
                approvalLineRepository.findAllByRequestAndDeleteYnFalseOrderByOrderNoAsc(request)
        );
    }

    @Override
    public ApprovalProcessResponse approve(Long userId, Long id, ApprovalApproveRequest request) {
        ApprovalRequest approvalRequest = getApprovalRequest(id);
        validateProcessable(approvalRequest);

        ApprovalLine currentLine = getCurrentPendingLine(approvalRequest);
        validateCurrentApprover(currentLine, userId);

        currentLine.approve(request.getComment());
        saveHistory(approvalRequest, ApprovalAction.APPROVE, request.getComment());
        approvalNotificationService.removePendingApproval(userId, approvalRequest.getId());

        ApprovalLine nextLine = findNextPendingLine(approvalRequest, currentLine.getOrderNo());
        if (nextLine == null) {
            approvalRequest.updateStatus(ApprovalStatus.APPROVED);
            return new ApprovalProcessResponse(ApprovalStatus.APPROVED, null);
        }

        approvalRequest.updateStatus(ApprovalStatus.IN_PROGRESS);
        approvalNotificationService.notifyPendingApproval(
                nextLine.getApprover().getId(),
                approvalRequest.getId()
        );

        return new ApprovalProcessResponse(
                ApprovalStatus.IN_PROGRESS,
                nextLine.getApprover().getName()
        );
    }

    @Override
    public ApprovalProcessResponse reject(Long userId, Long id, ApprovalRejectRequest request) {
        ApprovalRequest approvalRequest = getApprovalRequest(id);
        validateProcessable(approvalRequest);

        ApprovalLine currentLine = getCurrentPendingLine(approvalRequest);
        validateCurrentApprover(currentLine, userId);

        currentLine.reject(request.getComment());
        approvalRequest.updateStatus(ApprovalStatus.REJECTED);
        saveHistory(approvalRequest, ApprovalAction.REJECT, request.getComment());
        approvalNotificationService.removePendingApproval(userId, approvalRequest.getId());

        return new ApprovalProcessResponse(ApprovalStatus.REJECTED, null);
    }

    private List<User> resolveApprovers(Long creatorId, List<Long> approverIds) {
        Set<Long> uniqueIds = new HashSet<>(approverIds);
        if (uniqueIds.size() != approverIds.size()) {
            throw new BusinessException(ErrorCode.INVALID_APPROVER);
        }

        List<User> approvers = new ArrayList<>();
        for (Long approverId : approverIds) {
            if (approverId.equals(creatorId)) {
                throw new BusinessException(ErrorCode.INVALID_APPROVER);
            }
            approvers.add(getUser(approverId));
        }
        return approvers;
    }

    private ApprovalRequest getApprovalRequest(Long id) {
        return approvalRequestRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.APPROVAL_NOT_FOUND));
    }

    private void validateProcessable(ApprovalRequest approvalRequest) {
        if (approvalRequest.getStatus() == ApprovalStatus.APPROVED
                || approvalRequest.getStatus() == ApprovalStatus.REJECTED) {
            throw new BusinessException(ErrorCode.APPROVAL_ALREADY_COMPLETED);
        }
    }

    private ApprovalLine getCurrentPendingLine(ApprovalRequest approvalRequest) {
        return approvalLineRepository.findAllByRequestAndDeleteYnFalseOrderByOrderNoAsc(approvalRequest).stream()
                .filter(line -> line.getStatus() == ApprovalStatus.PENDING)
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.APPROVAL_ALREADY_COMPLETED));
    }

    private void validateCurrentApprover(ApprovalLine currentLine, Long userId) {
        if (!currentLine.getApprover().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.APPROVAL_FORBIDDEN);
        }
    }

    private ApprovalLine findNextPendingLine(ApprovalRequest approvalRequest, int currentOrderNo) {
        return approvalLineRepository.findAllByRequestAndDeleteYnFalseOrderByOrderNoAsc(approvalRequest).stream()
                .filter(line -> line.getStatus() == ApprovalStatus.PENDING)
                .filter(line -> line.getOrderNo() > currentOrderNo)
                .findFirst()
                .orElse(null);
    }

    private void saveHistory(ApprovalRequest approvalRequest, ApprovalAction action, String comment) {
        approvalHistoryRepository.save(ApprovalHistory.builder()
                .request(approvalRequest)
                .action(action)
                .comment(comment)
                .build());
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .filter(user -> !user.isDeleteYn())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }
}
