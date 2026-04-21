package com.dearfloral.common.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    private final String code;

    public NotFoundException(String message) {
        this(ErrorCode.NOT_FOUND, message);
    }

    public NotFoundException(String code, String message) {
        super(message);
        this.code = code;
    }
}
