package com.example.backend.oems.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Data Transfer Objects for Authentication endpoints
 */
public class AuthDtos {
    
    public static class CompleteProfileRequest {
        private String name;
        private String email;
        private String phone;
        
        public CompleteProfileRequest() {}
        
        public CompleteProfileRequest(String name, String email, String phone) {
            this.name = name;
            this.email = email;
            this.phone = phone;
        }
        
        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
    }
    
    public static class GoogleLoginRequest {
        @JsonProperty("idToken")
        private String idToken;
        
        public GoogleLoginRequest() {}
        
        public GoogleLoginRequest(String idToken) {
            this.idToken = idToken;
        }
        
        public String getIdToken() { return idToken; }
        public void setIdToken(String idToken) { this.idToken = idToken; }
    }
}