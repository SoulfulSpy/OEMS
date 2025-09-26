package com.example.backend.oems.exception;

/**
 * Exception thrown when business validation fails
 */
public class ValidationException extends OemsException {
    
    public ValidationException(String message) {
        super(message, "VALIDATION_ERROR");
    }
    
    public ValidationException(String message, Throwable cause) {
        super(message, "VALIDATION_ERROR", cause);
    }
}