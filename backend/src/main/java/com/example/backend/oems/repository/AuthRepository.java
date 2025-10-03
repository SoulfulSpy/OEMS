package com.example.backend.oems.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.backend.oems.entity.Auth;

/**
 * Repository for managing Auth entities (JWT tokens)
 */
@Repository
public interface AuthRepository extends JpaRepository<Auth, UUID> {
    
    /**
     * Find auth record by user ID
     */
    Optional<Auth> findByUserId(UUID userId);
    
    /**
     * Find auth record by access token
     */
    Optional<Auth> findByAccessToken(String accessToken);
    
    /**
     * Find auth record by refresh token
     */
    Optional<Auth> findByRefreshToken(String refreshToken);
    
    /**
     * Delete all auth records for a user (logout)
     */
    @Modifying
    @Query("DELETE FROM Auth a WHERE a.userId = :userId")
    void deleteAllByUserId(@Param("userId") UUID userId);
    
    /**
     * Delete expired tokens
     */
    @Modifying
    @Query("DELETE FROM Auth a WHERE a.tokenExpiry < CURRENT_TIMESTAMP")
    void deleteExpiredTokens();
    
    /**
     * Check if access token exists and is not expired
     */
    @Query("SELECT COUNT(a) > 0 FROM Auth a WHERE a.accessToken = :accessToken AND a.tokenExpiry > CURRENT_TIMESTAMP")
    boolean isAccessTokenValid(@Param("accessToken") String accessToken);
    
    /**
     * Check if refresh token exists and is not expired
     */
    @Query("SELECT COUNT(a) > 0 FROM Auth a WHERE a.refreshToken = :refreshToken AND a.tokenExpiry > CURRENT_TIMESTAMP")
    boolean isRefreshTokenValid(@Param("refreshToken") String refreshToken);
}