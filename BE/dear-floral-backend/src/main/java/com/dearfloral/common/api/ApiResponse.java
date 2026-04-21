package com.dearfloral.common.api;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiResponse<T> {
    private boolean success;
    private String code;
    private String message;
    private T data;
    private Object meta;
    private List<FieldError> errors;

    public static <T> ApiResponse<T> success(String code, String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .code(code)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> success(String code, String message, T data, Object meta) {
        return ApiResponse.<T>builder()
                .success(true)
                .code(code)
                .message(message)
                .data(data)
                .meta(meta)
                .build();
    }

    public static <T> ApiResponse<T> error(String code, String message, List<FieldError> errors) {
        return ApiResponse.<T>builder()
                .success(false)
                .code(code)
                .message(message)
                .errors(errors)
                .build();
    }

    @Getter
    @Builder
    public static class FieldError {
        private String field;
        private String message;
    }
}
