package com.example.approval.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PageResponse<T> {

    private final List<T> content;
    private final int page;
    private final int size;
}
