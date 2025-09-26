package com.example.backend.oems.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Application configuration properties
 */
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    
    private Security security = new Security();
    private Twilio twilio = new Twilio();
    private Cors cors = new Cors();
    private String environment = "development";
    
    public static class Security {
        private Jwt jwt = new Jwt();
        private Otp otp = new Otp();
        
        public static class Jwt {
            private String secret;
            private long expiration = 86400000; // 24 hours
            
            public String getSecret() { return secret; }
            public void setSecret(String secret) { this.secret = secret; }
            
            public long getExpiration() { return expiration; }
            public void setExpiration(long expiration) { this.expiration = expiration; }
        }
        
        public static class Otp {
            private long expiration = 300000; // 5 minutes
            
            public long getExpiration() { return expiration; }
            public void setExpiration(long expiration) { this.expiration = expiration; }
        }
        
        public Jwt getJwt() { return jwt; }
        public void setJwt(Jwt jwt) { this.jwt = jwt; }
        
        public Otp getOtp() { return otp; }
        public void setOtp(Otp otp) { this.otp = otp; }
    }
    
    public static class Twilio {
        private String accountSid;
        private String authToken;
        private String fromNumber;
        private String messagingServiceSid;
        
        public String getAccountSid() { return accountSid; }
        public void setAccountSid(String accountSid) { this.accountSid = accountSid; }
        
        public String getAuthToken() { return authToken; }
        public void setAuthToken(String authToken) { this.authToken = authToken; }
        
        public String getFromNumber() { return fromNumber; }
        public void setFromNumber(String fromNumber) { this.fromNumber = fromNumber; }
        
        public String getMessagingServiceSid() { return messagingServiceSid; }
        public void setMessagingServiceSid(String messagingServiceSid) { this.messagingServiceSid = messagingServiceSid; }
    }
    
    public static class Cors {
        private String allowedOrigins = "http://localhost:5173";
        private String allowedMethods = "GET,POST,PUT,DELETE,OPTIONS";
        private String allowedHeaders = "*";
        private boolean allowCredentials = true;
        
        public String getAllowedOrigins() { return allowedOrigins; }
        public void setAllowedOrigins(String allowedOrigins) { this.allowedOrigins = allowedOrigins; }
        
        public String getAllowedMethods() { return allowedMethods; }
        public void setAllowedMethods(String allowedMethods) { this.allowedMethods = allowedMethods; }
        
        public String getAllowedHeaders() { return allowedHeaders; }
        public void setAllowedHeaders(String allowedHeaders) { this.allowedHeaders = allowedHeaders; }
        
        public boolean isAllowCredentials() { return allowCredentials; }
        public void setAllowCredentials(boolean allowCredentials) { this.allowCredentials = allowCredentials; }
    }
    
    public Security getSecurity() { return security; }
    public void setSecurity(Security security) { this.security = security; }
    
    public Twilio getTwilio() { return twilio; }
    public void setTwilio(Twilio twilio) { this.twilio = twilio; }
    
    public Cors getCors() { return cors; }
    public void setCors(Cors cors) { this.cors = cors; }
    
    public String getEnvironment() { return environment; }
    public void setEnvironment(String environment) { this.environment = environment; }
}