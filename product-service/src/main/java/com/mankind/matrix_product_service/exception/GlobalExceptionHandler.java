package com.mankind.matrix_product_service.exception;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @Value("${spring.profiles.active:prod}")
    private String activeProfile;

    private List<String> sanitizeStackTrace(StackTraceElement[] stackTrace) {
        return Arrays.stream(stackTrace)
                .limit(5) // Limit to first 5 frames for security
                .map(StackTraceElement::toString)
                .collect(Collectors.toList());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        log.error("Resource not found: {}", ex.getMessage());
        return new ResponseEntity<>(
            ErrorResponse.of(HttpStatus.NOT_FOUND.value(), ex.getMessage()),
            HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResourceException(DuplicateResourceException ex, WebRequest request) {
        log.error("Duplicate resource: {}", ex.getMessage());
        return new ResponseEntity<>(
            ErrorResponse.of(HttpStatus.CONFLICT.value(), ex.getMessage()),
            HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<ErrorResponse> handleInvalidOperationException(InvalidOperationException ex, WebRequest request) {
        log.error("Invalid operation: {}", ex.getMessage());
        return new ResponseEntity<>(
            ErrorResponse.of(HttpStatus.BAD_REQUEST.value(), ex.getMessage()),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
        log.error("Unexpected error occurred", ex);
        
        if ("dev".equals(activeProfile)) {
            return new ResponseEntity<>(
                ErrorResponse.of(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    ex.getMessage(),
                    ex.getClass().getName(),
                    sanitizeStackTrace(ex.getStackTrace())
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        } else {
            return new ResponseEntity<>(
                ErrorResponse.of(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "An unexpected error occurred"
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
