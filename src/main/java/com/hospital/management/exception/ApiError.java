package com.hospital.management.exception;

import java.time.LocalDateTime;
import java.util.Map;

public class ApiError {
    private int status;
    private String message;
    private LocalDateTime timestamp = LocalDateTime.now();
    private Map<String, String> validationErrors;

    public ApiError(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public ApiError(int status, String message, Map<String, String> validationErrors) {
        this.status = status;
        this.message = message;
        this.validationErrors = validationErrors;
    }

    public int getStatus() { return status; }
    public String getMessage() { return message; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public Map<String, String> getValidationErrors() { return validationErrors; }
}
