package com.example.backend.oems.controller;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.oems.entity.OtpCode;
import com.example.backend.oems.entity.User;
import com.example.backend.oems.repository.OtpCodeRepository;
import com.example.backend.oems.service.JwtService;
import com.example.backend.oems.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private final UserService userService;
    private final OtpCodeRepository otpRepository;
    private final JwtService jwtService;
    private final SecureRandom secureRandom;
    
    // Rate limiting: phone -> (count, lastReset)
    private final Map<String, RateLimitInfo> otpRateLimit = new ConcurrentHashMap<>();
    
    // Phone number validation pattern
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[1-9]\\d{1,14}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    
    // Cookie configuration (similar to Express.js cookie options)
    private static final String ACCESS_TOKEN_COOKIE = "access_token";
    private static final String REFRESH_TOKEN_COOKIE = "refresh_token";
    private static final int ACCESS_TOKEN_MAX_AGE = 24 * 60 * 60; // 24 hours
    private static final int REFRESH_TOKEN_MAX_AGE = 7 * 24 * 60 * 60; // 7 days
    
    public AuthController(UserService userService, OtpCodeRepository otpRepository, JwtService jwtService) {
        this.userService = userService;
        this.otpRepository = otpRepository;
        this.jwtService = jwtService;
        this.secureRandom = new SecureRandom();
    }
    
    private static class RateLimitInfo {
        int count;
        Instant lastReset;
        
        RateLimitInfo(int count, Instant lastReset) {
            this.count = count;
            this.lastReset = lastReset;
        }
    }
    
    @PostMapping("/send-otp")
    public ResponseEntity<Object> sendOtp(@RequestBody Map<String, String> body) {
        String phone = body.get("phone");
        
        // Input validation
        if (phone == null || phone.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Phone number is required"));
        }
        
        // Phone number formatting and validation
        phone = normalizePhoneNumber(phone);
        if (!isValidPhoneNumber(phone)) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid phone number format"));
        }
        
        // Rate limiting: max 3 OTPs per phone per hour
        if (!checkRateLimit(phone)) {
            return ResponseEntity.status(429).body(Map.of(
                "message", "Too many OTP requests. Please try again later.",
                "retryAfter", "3600"
            ));
        }
        
        // Generate cryptographically secure OTP
        String code = generateSecureOtp();
        
        // Create OTP with shorter expiry (3 minutes for better security)
        OtpCode otp = new OtpCode();
        otp.setPhone(phone);
        otp.setCode(code);
        otp.setExpiresAt(Instant.now().plusSeconds(180)); // 3 minutes
        otpRepository.save(otp);
        
        // Development mode: show OTP in response
        boolean includeOtp = "dev".equalsIgnoreCase(System.getenv("APP_ENV"));
        if (includeOtp) {
            System.out.println("[OTP] Dev mode OTP for " + phone + ": " + code);
            return ResponseEntity.ok(Map.of("message", "OTP sent", "otp", code));
        }
        
        System.out.println("[OTP] Production OTP for " + maskPhoneNumber(phone) + ": " + code);
        return ResponseEntity.ok(Map.of("message", "OTP sent"));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<Object> verifyOtp(@RequestBody Map<String, String> body) {
        String phone = body.get("phone");
        String otp = body.get("otp");
        
        // Input validation
        if (phone == null || phone.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Phone number is required"));
        }
        
        if (otp == null || otp.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "OTP is required"));
        }
        
        phone = normalizePhoneNumber(phone);
        
        var valid = otpRepository.findTopByPhoneAndExpiresAtAfterOrderByIdDesc(phone, Instant.now())
                .filter(o -> o.getCode().equals(otp))
                .isPresent();
        
        if (!valid) {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid or expired OTP"));
        }
        
        boolean isNewUser = userService.findByPhone(phone).isEmpty();
        if (isNewUser) {
            return ResponseEntity.ok(Map.of(
                "isNewUser", true, 
                "message", "OTP verified. Please complete your profile."
            ));
        }
        
        // Generate JWT tokens for existing user (like Express.js res.cookie())
        User user = userService.findByPhone(phone).get();
        return setAuthCookiesAndRespond(user, "Login successful", false);
    }

    @PostMapping("/complete-profile")
    public ResponseEntity<Object> completeProfile(@RequestBody Map<String, String> req) {
        try {
            // Comprehensive input validation
            String name = req.get("name");
            String email = req.get("email");
            String phone = req.get("phone");
            
            // Validate required fields
            if (name == null || name.trim().length() < 2 || name.trim().length() > 100) {
                return ResponseEntity.badRequest().body(Map.of("message", "Name must be between 2 and 100 characters"));
            }
            
            if (email == null || !isValidEmail(email)) {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid email format"));
            }
            
            phone = normalizePhoneNumber(phone);
            if (!isValidPhoneNumber(phone)) {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid phone number format"));
            }
            
            // Check for duplicate email
            Optional<User> existingUserByEmail = userService.findByEmail(email);
            if (existingUserByEmail.isPresent() && !existingUserByEmail.get().getPhoneNumber().equals(phone)) {
                return ResponseEntity.badRequest().body(Map.of("message", "Email is already registered with another account"));
            }
            
            // Check if user already exists
            var existingUser = userService.findByPhone(phone);
            
            User user;
            boolean isNewUser = false;
            if (existingUser.isPresent()) {
                // Update existing user
                user = existingUser.get();
                user.setFullName(name.trim());
                user.setEmail(email.toLowerCase().trim());
            } else {
                // Create new user
                user = new User();
                user.setFullName(name.trim());
                user.setEmail(email.toLowerCase().trim());
                user.setPhoneNumber(phone);
                isNewUser = true;
            }
            
            User savedUser = userService.save(user);
            
            // Generate JWT tokens and set cookies (like Express.js res.cookie())
            return setAuthCookiesAndRespond(savedUser, 
                isNewUser ? "Profile created successfully! Welcome to OEMS." : "Profile updated successfully.",
                true);
            
        } catch (Exception e) {
            System.err.println("[ERROR] Profile completion failed: " + e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "error", "Failed to complete profile",
                "message", "An error occurred while processing your profile. Please try again."
            ));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<Object> refreshTokens(
            @CookieValue(value = REFRESH_TOKEN_COOKIE, required = false) String refreshToken) {
        
        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.status(401).body(Map.of(
                "success", false,
                "message", "Refresh token is required"
            ));
        }
        
        // Validate refresh token
        if (!jwtService.validateRefreshToken(refreshToken)) {
            return clearAuthCookiesAndRespond("Invalid or expired refresh token", 401);
        }
        
        // Extract user ID from refresh token
        Optional<UUID> userIdOpt = jwtService.extractUserId(refreshToken);
        if (userIdOpt.isEmpty()) {
            return clearAuthCookiesAndRespond("Invalid refresh token", 401);
        }
        
        // Get user from database
        Optional<User> userOpt = userService.findById(userIdOpt.get());
        if (userOpt.isEmpty()) {
            return clearAuthCookiesAndRespond("User not found", 401);
        }
        
        // Generate new token pair
        User user = userOpt.get();
        Optional<JwtService.TokenPair> newTokensOpt = jwtService.refreshTokens(refreshToken, user);
        
        if (newTokensOpt.isEmpty()) {
            return clearAuthCookiesAndRespond("Failed to refresh tokens", 401);
        }
        
        JwtService.TokenPair newTokens = newTokensOpt.get();
        
        // Set new cookies (like Express.js res.cookie())
        ResponseCookie accessTokenCookie = createAccessTokenCookie(newTokens.getAccessToken());
        ResponseCookie refreshTokenCookie = createRefreshTokenCookie(newTokens.getRefreshToken());
        
        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
            .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
            .body(Map.of(
                "success", true,
                "message", "Tokens refreshed successfully"
            ));
    }
    
    @PostMapping("/logout")
    public ResponseEntity<Object> logout(
            @CookieValue(value = ACCESS_TOKEN_COOKIE, required = false) String accessToken) {
        
        // Extract user ID from access token if present
        if (accessToken != null && !accessToken.isBlank()) {
            Optional<UUID> userIdOpt = jwtService.extractUserId(accessToken);
            if (userIdOpt.isPresent()) {
                // Invalidate all tokens for the user in database
                jwtService.logout(userIdOpt.get());
            }
        }
        
        // Clear cookies (like Express.js res.clearCookie())
        return clearAuthCookiesAndRespond("Logged out successfully", 200);
    }
    
    @GetMapping("/me")
    public ResponseEntity<Object> getCurrentUser(
            @CookieValue(value = ACCESS_TOKEN_COOKIE, required = false) String cookieToken,
            HttpServletRequest request) {
        
        String accessToken = null;
        
        // Try to get token from cookie first (preferred method)
        if (cookieToken != null && !cookieToken.isBlank()) {
            accessToken = cookieToken;
        } else {
            // Fallback: Try to get token from Authorization header (Bearer token)
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                accessToken = authHeader.substring(7);
            }
        }
        
        if (accessToken == null || accessToken.isBlank()) {
            return ResponseEntity.status(401).body(Map.of(
                "success", false,
                "message", "Access token is required"
            ));
        }
        
        // Validate access token
        if (!jwtService.validateAccessToken(accessToken)) {
            return clearAuthCookiesAndRespond("Invalid or expired access token", 401);
        }
        
        // Extract user info from token
        Optional<JwtService.UserInfo> userInfoOpt = jwtService.extractUserInfo(accessToken);
        if (userInfoOpt.isEmpty()) {
            return clearAuthCookiesAndRespond("Invalid access token", 401);
        }
        
        JwtService.UserInfo userInfo = userInfoOpt.get();
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "user", Map.of(
                "id", userInfo.getUserId(),
                "email", userInfo.getEmail(),
                "fullName", userInfo.getName(),
                "phoneNumber", userInfo.getPhone()
            )
        ));
    }

    @PostMapping("/google")
    public ResponseEntity<Object> googleLogin(@RequestBody Map<String, String> body) {
        String idToken = body.get("idToken");
        if (idToken == null || idToken.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "idToken is required"
            ));
        }
        
        // TODO: Implement real Google OAuth2 verification
        // For now, return dummy success response for testing
        String testEmail = "test.google.user@example.com";
        var testUserOpt = userService.findByEmail(testEmail);
        User testUser = testUserOpt.orElseGet(() -> {
            User u = new User();
            u.setEmail(testEmail);
            u.setFullName("Test Google User");
            u.setPhoneNumber("+10000000001");
            return userService.save(u);
        });
        
        return setAuthCookiesAndRespond(testUser, "Google login successful (dev mode - dummy implementation)", false);
        // TODO: Implement real Google OAuth2 verification in production
    }

    @GetMapping("/test-docs")
    public ResponseEntity<Object> getTestDocumentation() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "API documentation endpoint test");
        response.put("status", "working");
        response.put("timestamp", Instant.now().toString());
        return ResponseEntity.ok(response);
    }
    
    // =====================
    // COOKIE UTILITY METHODS (Similar to Express.js cookie handling)
    // =====================
    
    /**
     * Set authentication cookies and respond (similar to Express.js res.cookie())
     * Also includes JWT tokens in response body for Postman collection compatibility
     */
    private ResponseEntity<Object> setAuthCookiesAndRespond(User user, String message, boolean includeSuccessFlag) {
        // Generate JWT tokens
        JwtService.TokenPair tokens = jwtService.generateTokenPair(user);
        
        // Create HTTP-only cookies (equivalent to Express.js res.cookie() with httpOnly: true)
        ResponseCookie accessTokenCookie = createAccessTokenCookie(tokens.getAccessToken());
        ResponseCookie refreshTokenCookie = createRefreshTokenCookie(tokens.getRefreshToken());
        
        Map<String, Object> responseBody = new HashMap<>();
        if (includeSuccessFlag) {
            responseBody.put("success", true);
        }
        
        // Include JWT tokens in response body for Postman collection compatibility
        responseBody.put("token", tokens.getAccessToken());
        responseBody.put("refreshToken", tokens.getRefreshToken());
        
        // User data with direct id field for Postman compatibility
        Map<String, Object> userData = Map.of(
            "id", user.getId(),
            "email", user.getEmail(),
            "fullName", user.getFullName(),
            "phoneNumber", user.getPhoneNumber(),
            "status", user.getStatus()
        );
        responseBody.put("user", userData);
        
        // Add direct id field for complete-profile compatibility
        if (includeSuccessFlag) {
            responseBody.put("id", user.getId());
        }
        
        responseBody.put("message", message);
        
        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
            .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
            .body(responseBody);
    }
    
    /**
     * Create access token cookie (equivalent to Express.js res.cookie with security options)
     */
    private ResponseCookie createAccessTokenCookie(String accessToken) {
        return ResponseCookie.from(ACCESS_TOKEN_COOKIE, accessToken)
            .httpOnly(true)                    // Prevent XSS attacks
            .secure(false)                     // Set to true for HTTPS in production
            .path("/")                         // Available to all routes
            .maxAge(ACCESS_TOKEN_MAX_AGE)      // 24 hours
            .sameSite("Strict")                // CSRF protection
            .build();
    }
    
    /**
     * Create refresh token cookie (equivalent to Express.js res.cookie with security options)
     */
    private ResponseCookie createRefreshTokenCookie(String refreshToken) {
        return ResponseCookie.from(REFRESH_TOKEN_COOKIE, refreshToken)
            .httpOnly(true)                    // Prevent XSS attacks
            .secure(false)                     // Set to true for HTTPS in production
            .path("/api/auth")                 // Restrict to auth endpoints only
            .maxAge(REFRESH_TOKEN_MAX_AGE)     // 7 days
            .sameSite("Strict")                // CSRF protection
            .build();
    }
    
    /**
     * Clear authentication cookies and respond (similar to Express.js res.clearCookie())
     */
    private ResponseEntity<Object> clearAuthCookiesAndRespond(String message, int statusCode) {
        ResponseCookie clearAccessToken = clearAccessTokenCookie();
        ResponseCookie clearRefreshToken = clearRefreshTokenCookie();
        
        return ResponseEntity.status(statusCode)
            .header(HttpHeaders.SET_COOKIE, clearAccessToken.toString())
            .header(HttpHeaders.SET_COOKIE, clearRefreshToken.toString())
            .body(Map.of(
                "success", false,
                "message", message
            ));
    }
    
    /**
     * Clear access token cookie (equivalent to Express.js res.clearCookie)
     */
    private ResponseCookie clearAccessTokenCookie() {
        return ResponseCookie.from(ACCESS_TOKEN_COOKIE, "")
            .httpOnly(true)
            .secure(false)  // Set to true for HTTPS in production
            .path("/")
            .maxAge(0)  // Expire immediately
            .build();
    }
    
    /**
     * Clear refresh token cookie (equivalent to Express.js res.clearCookie)
     */
    private ResponseCookie clearRefreshTokenCookie() {
        return ResponseCookie.from(REFRESH_TOKEN_COOKIE, "")
            .httpOnly(true)
            .secure(false)  // Set to true for HTTPS in production
            .path("/api/auth")
            .maxAge(0)  // Expire immediately
            .build();
    }
    
    // =====================
    // HELPER METHODS
    // =====================
    
    private String normalizePhoneNumber(String phone) {
        // Remove all non-digit characters except +
        phone = phone.replaceAll("[^+\\d]", "");
        
        // Add + if not present and phone starts with country code
        if (!phone.startsWith("+") && phone.length() > 10) {
            phone = "+" + phone;
        }
        
        return phone;
    }
    
    private boolean isValidPhoneNumber(String phone) {
        return PHONE_PATTERN.matcher(phone).matches();
    }
    
    private boolean checkRateLimit(String phone) {
        Instant now = Instant.now();
        RateLimitInfo info = otpRateLimit.get(phone);
        
        if (info == null) {
            otpRateLimit.put(phone, new RateLimitInfo(1, now));
            return true;
        }
        
        // Reset counter if an hour has passed
        if (now.isAfter(info.lastReset.plus(1, ChronoUnit.HOURS))) {
            otpRateLimit.put(phone, new RateLimitInfo(1, now));
            return true;
        }
        
        // Check if under limit
        if (info.count < 3) {
            info.count++;
            return true;
        }
        
        return false; // Rate limit exceeded
    }
    
    private String generateSecureOtp() {
        // Generate 6-digit OTP using cryptographically secure random
        int otp = 100000 + secureRandom.nextInt(900000);
        return String.valueOf(otp);
    }
    
    private String maskPhoneNumber(String phone) {
        if (phone.length() <= 4) return "****";
        return phone.substring(0, 3) + "****" + phone.substring(phone.length() - 2);
    }
    
    private boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.toLowerCase().trim()).matches();
    }
}
