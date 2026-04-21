package com.dearfloral.module.categories.dto;

public record CategoryResponse(
        Long categoryId,
        String name,
        String description,
        String status
) {
}
