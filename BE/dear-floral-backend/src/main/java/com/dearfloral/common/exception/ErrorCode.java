package com.dearfloral.common.exception;

public final class ErrorCode {
    private ErrorCode() {
    }

    public static final String VALIDATION_ERROR = "VALIDATION_ERROR";
    public static final String NOT_FOUND = "NOT_FOUND";
    public static final String BUSINESS_RULE_VIOLATION = "BUSINESS_RULE_VIOLATION";
    public static final String UNAUTHORIZED = "UNAUTHORIZED";
    public static final String FORBIDDEN = "FORBIDDEN";
    public static final String BAD_REQUEST = "BAD_REQUEST";
    public static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
}
