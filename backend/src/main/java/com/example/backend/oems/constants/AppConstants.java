package com.example.backend.oems.constants;

/**
 * Application constants
 */
public final class AppConstants {
    
    private AppConstants() {
        // Utility class
    }
    
    // API Endpoints
    public static final String API_BASE_PATH = "/api";
    public static final String AUTH_BASE_PATH = API_BASE_PATH + "/auth";
    public static final String USER_BASE_PATH = API_BASE_PATH + "/users";
    public static final String TRIP_BASE_PATH = API_BASE_PATH + "/trips";
    public static final String PAYMENT_BASE_PATH = API_BASE_PATH + "/payments";
    
    // Security Constants
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String JWT_CLAIM_USER_ID = "userId";
    public static final String JWT_CLAIM_PHONE = "phone";
    
    // OTP Constants
    public static final int OTP_LENGTH = 6;
    public static final String OTP_CHARACTERS = "0123456789";
    
    // Validation Messages
    public static final String INVALID_PHONE_MESSAGE = "Invalid phone number format";
    public static final String INVALID_EMAIL_MESSAGE = "Invalid email format";
    public static final String REQUIRED_FIELD_MESSAGE = " is required";
    
    // Error Codes
    public static final String VALIDATION_ERROR = "VALIDATION_ERROR";
    public static final String RESOURCE_NOT_FOUND = "RESOURCE_NOT_FOUND";
    public static final String UNAUTHORIZED = "UNAUTHORIZED";
    public static final String FORBIDDEN = "FORBIDDEN";
    public static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
    
    // Environment Names
    public static final String ENV_DEVELOPMENT = "development";
    public static final String ENV_PRODUCTION = "production";
    public static final String ENV_STAGING = "staging";
    
    // Default Values
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;
}