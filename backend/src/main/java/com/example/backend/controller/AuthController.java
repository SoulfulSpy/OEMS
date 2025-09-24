package com.example.backend.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.entity.OtpCode;
import com.example.backend.entity.User;
import com.example.backend.repository.OtpCodeRepository;
import com.example.backend.service.UserService;

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
    public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> body) {
        String phone = body.get("phone");
        if (phone == null || phone.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "phone is required"));
        }
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
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> body) {
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
    public ResponseEntity<User> completeProfile(@RequestBody CompleteProfileRequest req) {
        User user = new User();
        user.setName(req.name());
        user.setEmail(req.email());
        user.setPhone(req.phone());
        return ResponseEntity.ok(userService.save(user));
    }

    public record CompleteProfileRequest(String name, String email, String phone) {}

    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> body) {
        String idToken = body.get("idToken");
        if (idToken == null || idToken.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "idToken is required"));
        }
        // Simple verification via Google tokeninfo endpoint (dev only)
        try {
            var uri = java.net.URI.create("https://oauth2.googleapis.com/tokeninfo?id_token=" + java.net.URLEncoder.encode(idToken, java.nio.charset.StandardCharsets.UTF_8));
            try (var in = uri.toURL().openStream()) {
                String json = new String(in.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
                // very light parsing to extract email and name
                String email = extractJsonValue(json, "email");
                String name = extractJsonValue(json, "name");
                if (email == null) {
                    return ResponseEntity.status(401).body(Map.of("message", "Invalid Google token"));
                }
                var userOpt = userService.findByEmail(email);
                User user = userOpt.orElseGet(() -> {
                    User u = new User();
                    u.setEmail(email);
                    u.setName(name != null ? name : email);
                    u.setPhone("+10000000000");
                    return userService.save(u);
                });
                return ResponseEntity.ok(Map.of("user", user));
            }
        } catch (java.io.IOException | IllegalArgumentException e) {
            return ResponseEntity.status(500).body(Map.of("message", "Google verification failed"));
        }
    }

    private static String extractJsonValue(String json, String key) {
        String pattern = "\"" + key + "\":\"";
        int i = json.indexOf(pattern);
        if (i < 0) return null;
        int start = i + pattern.length();
        int end = json.indexOf('"', start);
        if (end < 0) return null;
        return json.substring(start, end);
    }
}


