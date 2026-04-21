package com.dearfloral.common.exception;

import com.dearfloral.common.api.ApiResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<ApiResponse.FieldError> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> ApiResponse.FieldError.builder()
                        .field(fieldError.getField())
                        .message(fieldError.getDefaultMessage())
                        .build())
                .toList();

        ApiResponse<Void> response = ApiResponse.error(
                ErrorCode.VALIDATION_ERROR,
                "Validation failed.",
                errors
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(ConstraintViolationException ex) {
        List<ApiResponse.FieldError> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(ApiResponse.FieldError.builder()
                    .field(violation.getPropertyPath().toString())
                    .message(violation.getMessage())
                    .build());
        }

        ApiResponse<Void> response = ApiResponse.error(
                ErrorCode.VALIDATION_ERROR,
                "Validation failed.",
                errors
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(NotFoundException ex) {
        ApiResponse<Void> response = ApiResponse.error(ex.getCode(), ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusiness(BusinessException ex) {
        ApiResponse<Void> response = ApiResponse.error(ex.getCode(), ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuth(AuthException ex) {
        ApiResponse<Void> response = ApiResponse.error(ex.getCode(), ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDenied(AccessDeniedException ex) {
        ApiResponse<Void> response = ApiResponse.error(ErrorCode.FORBIDDEN, "Access denied.", null);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(IllegalArgumentException ex) {
        ApiResponse<Void> response = ApiResponse.error(ErrorCode.BAD_REQUEST, ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        ApiResponse<Void> response = ApiResponse.error(ErrorCode.BAD_REQUEST, "Malformed request payload.", null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Void>> handleSpringAuthentication(AuthenticationException ex) {
        ApiResponse<Void> response = ApiResponse.error(ErrorCode.UNAUTHORIZED, "Authentication failed.", null);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnhandled(Exception ex) {
        log.error("Unhandled system exception", ex);
        ApiResponse<Void> response = ApiResponse.error(
                ErrorCode.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred.",
                null
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
