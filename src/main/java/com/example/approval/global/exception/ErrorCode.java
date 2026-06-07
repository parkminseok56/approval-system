package com.example.approval.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INVALID_INPUT(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "Invalid input data"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "Invalid token"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND", "User not found"),
    DEPARTMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND", "Department not found"),
    APPROVAL_NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND", "Approval request not found"),
    APPROVAL_FORBIDDEN(HttpStatus.FORBIDDEN, "FORBIDDEN", "Not allowed to process this approval"),
    APPROVAL_ALREADY_COMPLETED(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "Approval request already completed"),
    INVALID_APPROVER(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "Invalid approver"),
    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "CONFLICT", "Username already exists");

    private final HttpStatus httpStatus;
    private final String error;
    private final String message;
}
