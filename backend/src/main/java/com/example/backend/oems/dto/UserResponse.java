package com.example.backend.oems.dto;

import java.time.Instant;
import java.util.UUID;

/**
 * DTO for User response (without sensitive information)
 */
public class UserResponse {
    
    private UUID id;
    private String name;
    private String email;
    private String phone;
    private Instant createdAt;
    
    public UserResponse() {}
    
    public UserResponse(UUID id, String name, String email, String phone, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.createdAt = createdAt;
    }
    
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}