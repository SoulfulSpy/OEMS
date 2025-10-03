package com.example.backend.oems.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.oems.constants.AppConstants;
import com.example.backend.oems.entity.Auth;
import com.example.backend.oems.entity.User;
import com.example.backend.oems.repository.AuthRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * JWT Service for token generation, validation, and management
 */
@Service
@Transactional
public class JwtService {
    
    private final AuthRepository authRepository;
    private final SecretKey signingKey;
    
    @Value("${app.security.jwt.expiration:86400000}") // 24 hours default
    private long accessTokenExpiration;
    
    private final long refreshTokenExpiration = 7 * 24 * 60 * 60 * 1000L; // 7 days
    
    public JwtService(AuthRepository authRepository, 
                     @Value("${app.security.jwt.secret}") String jwtSecret) {
        this.authRepository = authRepository;
        this.signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
    
    /**
     * Generate access token for user
     */
    public String generateAccessToken(User user) {
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim(AppConstants.JWT_CLAIM_USER_ID, user.getId().toString())
                .claim(AppConstants.JWT_CLAIM_PHONE, user.getPhoneNumber())
                .claim("email", user.getEmail())
                .claim("name", user.getFullName())
                .claim("type", "access")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(signingKey)
                .compact();
    }
    
    /**
     * Generate refresh token for user
     */
    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim(AppConstants.JWT_CLAIM_USER_ID, user.getId().toString())
                .claim("type", "refresh")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(signingKey)
                .compact();
    }
    
    /**
     * Generate both access and refresh tokens for user
     */
    public TokenPair generateTokenPair(User user) {
        String accessToken = generateAccessToken(user);
        String refreshToken = generateRefreshToken(user);
        
        // Store in database
        storeTokens(user.getId(), accessToken, refreshToken);
        
        return new TokenPair(accessToken, refreshToken);
    }
    
    /**
     * Store tokens in database
     */
    public void storeTokens(UUID userId, String accessToken, String refreshToken) {
        // Remove existing tokens for user
        authRepository.deleteAllByUserId(userId);
        
        // Create new auth record
        Auth auth = new Auth();
        auth.setUserId(userId);
        auth.setAccessToken(accessToken);
        auth.setRefreshToken(refreshToken);
        auth.setTokenExpiry(Instant.now().plus(accessTokenExpiration, ChronoUnit.MILLIS));
        
        authRepository.save(auth);
    }
    
    /**
     * Validate access token
     */
    public boolean validateAccessToken(String token) {
        try {
            Claims claims = parseToken(token);
            String tokenType = claims.get("type", String.class);
            
            if (!"access".equals(tokenType)) {
                return false;
            }
            
            // Check if token exists in database and is not expired
            return authRepository.isAccessTokenValid(token);
        } catch (JwtException e) {
            System.err.println("[JWT] Access token validation failed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Validate refresh token
     */
    public boolean validateRefreshToken(String token) {
        try {
            Claims claims = parseToken(token);
            String tokenType = claims.get("type", String.class);
            
            if (!"refresh".equals(tokenType)) {
                return false;
            }
            
            // Check if token exists in database and is not expired
            return authRepository.isRefreshTokenValid(token);
        } catch (JwtException e) {
            System.err.println("[JWT] Refresh token validation failed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Extract user ID from token
     */
    public Optional<UUID> extractUserId(String token) {
        try {
            Claims claims = parseToken(token);
            String userIdStr = claims.get(AppConstants.JWT_CLAIM_USER_ID, String.class);
            if (userIdStr == null || userIdStr.isEmpty()) {
                System.err.println("[JWT] User ID claim is null or empty in token");
                return Optional.empty();
            }
            return Optional.of(UUID.fromString(userIdStr));
        } catch (JwtException e) {
            System.err.println("[JWT] Token parsing failed: " + e.getMessage());
            return Optional.empty();
        } catch (IllegalArgumentException e) {
            System.err.println("[JWT] Invalid UUID format in user ID claim: " + e.getMessage());
            return Optional.empty();
        } catch (Exception e) {
            System.err.println("[JWT] Unexpected error extracting user ID: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            return Optional.empty();
        }
    }
    
    /**
     * Extract user information from token
     */
    public Optional<UserInfo> extractUserInfo(String token) {
        try {
            Claims claims = parseToken(token);
            String userIdStr = claims.get(AppConstants.JWT_CLAIM_USER_ID, String.class);
            if (userIdStr == null || userIdStr.isEmpty()) {
                System.err.println("[JWT] User ID claim is null or empty in token");
                return Optional.empty();
            }
            
            UserInfo userInfo = new UserInfo(
                UUID.fromString(userIdStr),
                claims.get(AppConstants.JWT_CLAIM_PHONE, String.class),
                claims.get("email", String.class),
                claims.get("name", String.class)
            );
            return Optional.of(userInfo);
        } catch (JwtException e) {
            System.err.println("[JWT] Token parsing failed: " + e.getMessage());
            return Optional.empty();
        } catch (IllegalArgumentException e) {
            System.err.println("[JWT] Invalid UUID format in user ID claim: " + e.getMessage());
            return Optional.empty();
        } catch (Exception e) {
            System.err.println("[JWT] Unexpected error extracting user info: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            return Optional.empty();
        }
    }
    
    /**
     * Refresh access token using refresh token
     */
    public Optional<TokenPair> refreshTokens(String refreshToken, User user) {
        if (!validateRefreshToken(refreshToken)) {
            return Optional.empty();
        }
        
        // Generate new token pair
        return Optional.of(generateTokenPair(user));
    }
    
    /**
     * Logout user by invalidating all tokens
     */
    public void logout(UUID userId) {
        authRepository.deleteAllByUserId(userId);
    }
    
    /**
     * Clean up expired tokens
     */
    public void cleanupExpiredTokens() {
        authRepository.deleteExpiredTokens();
    }
    
    /**
     * Parse JWT token and extract claims
     */
    private Claims parseToken(String token) throws JwtException {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    
    /**
     * Get access token expiration in milliseconds
     */
    public long getAccessTokenExpiration() {
        return accessTokenExpiration;
    }
    
    /**
     * Get refresh token expiration in milliseconds
     */
    public long getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }
    
    /**
     * Token pair data class
     */
    public static class TokenPair {
        private final String accessToken;
        private final String refreshToken;
        
        public TokenPair(String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }
        
        public String getAccessToken() { return accessToken; }
        public String getRefreshToken() { return refreshToken; }
    }
    
    /**
     * User info extracted from JWT token
     */
    public static class UserInfo {
        private final UUID userId;
        private final String phone;
        private final String email;
        private final String name;
        
        public UserInfo(UUID userId, String phone, String email, String name) {
            this.userId = userId;
            this.phone = phone;
            this.email = email;
            this.name = name;
        }
        
        public UUID getUserId() { return userId; }
        public String getPhone() { return phone; }
        public String getEmail() { return email; }
        public String getName() { return name; }
    }
}