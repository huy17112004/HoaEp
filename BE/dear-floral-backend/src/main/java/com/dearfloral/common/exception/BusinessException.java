package com.dearfloral.common.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final String code;

    public BusinessException(String message) {
        this(ErrorCode.BUSINESS_RULE_VIOLATION, message);
    }

    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
    }
}
