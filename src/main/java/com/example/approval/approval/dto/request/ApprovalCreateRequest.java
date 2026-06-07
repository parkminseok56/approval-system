package com.example.approval.approval.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ApprovalCreateRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotEmpty
    @Size(min = 1, max = 3)
    private List<Long> approverIds;
}
