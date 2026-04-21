package com.dearfloral.common.exception;

import lombok.Getter;

@Getter
public class AuthException extends RuntimeException {
    private final String code;

    public AuthException(String message) {
        this(ErrorCode.UNAUTHORIZED, message);
    }

    public AuthException(String code, String message) {
        super(message);
        this.code = code;
    }
}
