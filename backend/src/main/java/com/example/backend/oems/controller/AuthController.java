package com.example.backend.oems.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.oems.entity.OtpCode;
import com.example.backend.oems.entity.User;
import com.example.backend.oems.repository.OtpCodeRepository;
import com.example.backend.oems.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final OtpCodeRepository otpRepository;

    public AuthController(UserService userService, OtpCodeRepository otpRepository) {
        this.userService = userService;
        this.otpRepository = otpRepository;
    }

    @PostMapping("/send-otp")
    public ResponseEntity<Object> sendOtp(@RequestBody Map<String, String> body) {
        String phone = body.get("phone");
        if (phone == null || phone.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "phone is required"));
        }
        // TODO: Enhance OTP generation and security
        // - Use cryptographically secure random number generator
        // - Implement rate limiting to prevent OTP spam (max 3 OTPs per phone per hour)
        // - Add phone number validation and formatting
        // - Consider using shorter expiry time (2-3 minutes) for better security
        // - Implement OTP retry mechanism with exponential backoff
        // - Add audit logging for OTP generation and verification attempts
        String code = String.valueOf(100000 + (int)(Math.random() * 900000));
        OtpCode otp = new OtpCode();
        otp.setPhone(phone);
        otp.setCode(code);
        otp.setExpiresAt(java.time.Instant.now().plusSeconds(300));
        otpRepository.save(otp);

        // Optional SMS via Twilio if env present; otherwise log in server logs
        String sid = System.getenv("TWILIO_SID");
        String token = System.getenv("TWILIO_TOKEN");
        String from = System.getenv("TWILIO_FROM");
        String messagingServiceSid = System.getenv("TWILIO_MESSAGING_SERVICE_SID");
        if (sid != null && token != null && (from != null || (messagingServiceSid != null && !messagingServiceSid.isBlank()))) {
            try {
                com.twilio.Twilio.init(sid, token);
                if (messagingServiceSid != null && !messagingServiceSid.isBlank()) {
                    // Preferred: send via Messaging Service SID (from left null explicitly)
                    com.twilio.rest.api.v2010.account.Message.creator(
                            new com.twilio.type.PhoneNumber(phone),
                            (com.twilio.type.PhoneNumber) null,
                            "Your OEMS OTP is " + code + " (valid 5 minutes)")
                        .setMessagingServiceSid(messagingServiceSid)
                        .create();
                } else {
                    // Fallback: use explicit From number
                    com.twilio.rest.api.v2010.account.Message.creator(
                            new com.twilio.type.PhoneNumber(phone),
                            new com.twilio.type.PhoneNumber(from),
                            "Your OEMS OTP is " + code + " (valid 5 minutes)")
                        .create();
                }
            } catch (Exception e) {
                System.out.println("[OTP] Twilio send failed, falling back to dev log. " + e.getMessage());
                System.out.println("[OTP] Dev mode OTP for " + phone + ": " + code);
            }
        } else {
            System.out.println("[OTP] Dev mode OTP for " + phone + ": " + code);
        }
        boolean includeOtp = "dev".equalsIgnoreCase(System.getenv("APP_ENV"));
        if (includeOtp) {
            return ResponseEntity.ok(Map.of("message", "OTP sent", "otp", code));
        }
        return ResponseEntity.ok(Map.of("message", "OTP sent"));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<Object> verifyOtp(@RequestBody Map<String, String> body) {
        String phone = body.get("phone");
        String otp = body.get("otp");
        if (otp == null || otp.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "OTP is required"));
        }
        var valid = otpRepository.findTopByPhoneAndExpiresAtAfterOrderByIdDesc(phone, java.time.Instant.now())
                .filter(o -> o.getCode().equals(otp))
                .isPresent();
        if (!valid) {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid or expired OTP"));
        }
        boolean isNewUser = userService.findByPhone(phone).isEmpty();
        if (isNewUser) {
            return ResponseEntity.ok(Map.of("isNewUser", true));
        }
        User user = userService.findByPhone(phone).get();
        return ResponseEntity.ok(Map.of("isNewUser", false, "user", user));
    }

    @PostMapping("/complete-profile")
    public ResponseEntity<Object> completeProfile(@RequestBody Map<String, String> req) {
        // TODO: Add proper input validation and JWT token authentication
        // - Validate JWT token to ensure user is authenticated
        // - Add comprehensive input validation (email format, name length, etc.)
        // - Implement duplicate email/phone checking with proper error messages
        // - Add profile image upload support
        // - Implement user role assignment (customer, driver, admin)
        // - Add profile completion tracking and status updates
        // - Send welcome email/SMS after profile completion
        try {
            String name = req.get("name");
            String email = req.get("email");
            String phone = req.get("phone");
            
            // Check if user already exists
            var existingUser = userService.findByPhone(phone);
            
            User user;
            if (existingUser.isPresent()) {
                // Update existing user
                user = existingUser.get();
                user.setFullName(name);
                user.setEmail(email);
            } else {
                // Create new user
                user = new User();
                user.setFullName(name);
                user.setEmail(email);
                user.setPhoneNumber(phone);
            }
            
            User savedUser = userService.save(user);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "Failed to complete profile",
                "message", e.getMessage()
            ));
        }
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
        // TODO:
        // In development/test mode, accept a dummy token for testing
        String appEnv = System.getenv("APP_ENV");
        if ("dev".equalsIgnoreCase(appEnv) && "dummy-google-token".equals(idToken)) {
            // Create or find test user for development
            String testEmail = "test.google.user@example.com";
            var userOpt = userService.findByEmail(testEmail);
            User user = userOpt.orElseGet(() -> {
                User u = new User();
                u.setEmail(testEmail);
                u.setFullName("Test Google User");
                u.setPhoneNumber("+10000000001");
                return userService.save(u);
            });
            return ResponseEntity.ok(Map.of(
                "success", true,
                "user", user,
                "message", "Google login successful (dev mode)"
            ));
        }
        
        // TODO: Replace basic Google token verification with proper OAuth2 implementation
        // - Use Google's official Java client library instead of manual HTTP calls
        // - Implement proper JWT signature validation
        // - Add token expiration and audience validation
        // - Handle Google API rate limits and errors gracefully
        // - Consider using Spring Security OAuth2 for better integration
        try {
            var uri = java.net.URI.create("https://oauth2.googleapis.com/tokeninfo?id_token=" + 
                java.net.URLEncoder.encode(idToken, java.nio.charset.StandardCharsets.UTF_8));
            try (var in = uri.toURL().openStream()) {
                String json = new String(in.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
                // Light parsing to extract email and name
                String email = extractJsonValue(json, "email");
                String name = extractJsonValue(json, "name");                if (email == null) {
                    return ResponseEntity.status(401).body(Map.of(
                        "success", false,
                        "message", "Invalid Google token - no email found"
                    ));
                }
                
                var userOpt = userService.findByEmail(email);
                User user = userOpt.orElseGet(() -> {
                    User u = new User();
                    u.setEmail(email);
                    u.setFullName(name != null ? name : email);
                    // TODO: Replace dummy phone number generation with proper phone verification flow
                    // - Prompt user to enter and verify their phone number after Google OAuth
                    // - Implement phone number verification step in the frontend
                    // - Store user as PENDING_PHONE_VERIFICATION status until verified
                    // - Don't allow ride booking until phone is verified and confirmed
                    u.setPhoneNumber("+10000000000");
                    try {
                        return userService.save(u);
                    } catch (Exception e) {
                        // Handle duplicate phone number by generating a unique one
                        u.setPhoneNumber("+1000000" + String.format("%04d", (int)(Math.random() * 10000)));
                        return userService.save(u);
                    }
                });
                
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "user", user,
                    "message", "Google login successful"
                ));
            }
        } catch (java.io.IOException e) {
            return ResponseEntity.status(401).body(Map.of(
                "success", false,
                "message", "Failed to verify Google token",
                "error", "Network error or invalid token"
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Invalid token format",
                "error", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "Google verification failed",
                "error", "Internal server error"
            ));
        }
    }

    // TODO: Replace manual JSON parsing with proper JSON library
    // - Use Jackson ObjectMapper or Gson for robust JSON parsing
    // - Add proper error handling for malformed JSON
    // - Handle JSON escape sequences and special characters
    // - Consider using Google's JWT library for token parsing
    private static String extractJsonValue(String json, String key) {
        String pattern = "\"" + key + "\":\"";
        int i = json.indexOf(pattern);
        if (i < 0) return null;
        int start = i + pattern.length();
        int end = json.indexOf('"', start);
        if (end < 0) return null;
        return json.substring(start, end);
    }
    
    @GetMapping("/test-docs")
    public ResponseEntity<Object> getTestDocumentation() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "API documentation endpoint test");
        response.put("status", "working");
        response.put("timestamp", java.time.Instant.now().toString());
        return ResponseEntity.ok(response);
    }
}


