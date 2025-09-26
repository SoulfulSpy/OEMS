package com.example.backend.oems.exception;

/**
 * Base class for all business logic exceptions in the OEMS application
 */
public class OemsException extends RuntimeException {
    
    private final String errorCode;
    
    public OemsException(String message) {
        super(message);
        this.errorCode = "OEMS_ERROR";
    }
    
    public OemsException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public OemsException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "OEMS_ERROR";
    }
    
    public OemsException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
}