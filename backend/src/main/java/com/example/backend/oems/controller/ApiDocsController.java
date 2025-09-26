package com.example.backend.oems.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiDocsController {

    @GetMapping("/documentation")
    public ResponseEntity<Object> getApiDocumentation() {
        Map<String, Object> apiDocs = new HashMap<>();
        
        // API Information
        Map<String, String> info = new HashMap<>();
        info.put("title", "OEMS API");
        info.put("version", "1.0");
        info.put("description", "Online Electric Mobility Services API");
        apiDocs.put("info", info);
        
        // Endpoints
        Map<String, Object> endpoints = new HashMap<>();
        
        // Auth endpoints
        Map<String, Object> authEndpoints = new HashMap<>();
        authEndpoints.put("POST /api/auth/send-otp", "Send OTP to phone number");
        authEndpoints.put("POST /api/auth/verify-otp", "Verify OTP code");
        authEndpoints.put("POST /api/auth/complete-profile", "Complete user profile");
        authEndpoints.put("POST /api/auth/google", "Google OAuth login");
        endpoints.put("Authentication", authEndpoints);
        
        // Ride endpoints
        Map<String, Object> rideEndpoints = new HashMap<>();
        rideEndpoints.put("POST /api/rides/estimate", "Get ride cost estimates");
        rideEndpoints.put("POST /api/rides/book", "Book a ride");
        rideEndpoints.put("GET /api/rides/{tripId}", "Get ride status");
        rideEndpoints.put("POST /api/rides/{tripId}/cancel", "Cancel a ride");
        endpoints.put("Rides", rideEndpoints);
        
        // Payment endpoints
        Map<String, Object> paymentEndpoints = new HashMap<>();
        paymentEndpoints.put("POST /api/payments/charge", "Process payment");
        endpoints.put("Payments", paymentEndpoints);
        
        apiDocs.put("endpoints", endpoints);
        
        // Example requests
        Map<String, Object> examples = new HashMap<>();
        
        Map<String, Object> authExamples = new HashMap<>();
        authExamples.put("send-otp", Map.of("phone", "+1234567890"));
        authExamples.put("verify-otp", Map.of("phone", "+1234567890", "otp", "123456"));
        authExamples.put("complete-profile", Map.of("name", "John Doe", "email", "john@example.com", "phone", "+1234567890"));
        authExamples.put("google-login", Map.of("idToken", "your-google-id-token-here"));
        examples.put("auth", authExamples);
        
        Map<String, Object> rideExamples = new HashMap<>();
        rideExamples.put("estimate", Map.of(
            "pickup", Map.of("latitude", 37.7749, "longitude", -122.4194, "address", "123 Main St"),
            "destination", Map.of("latitude", 37.7849, "longitude", -122.4094, "address", "456 Oak St")
        ));
        rideExamples.put("book", Map.of(
            "pickup", Map.of("latitude", 37.7749, "longitude", -122.4194, "address", "123 Main St"),
            "destination", Map.of("latitude", 37.7849, "longitude", -122.4094, "address", "456 Oak St"),
            "rideOption", Map.of("id", "oems-go", "name", "OEMS Go", "type", "Hatchback", "price", 120),
            "userPhone", "+1234567890"
        ));
        examples.put("rides", rideExamples);
        
        apiDocs.put("examples", examples);
        
        return ResponseEntity.ok(apiDocs);
    }
    
    @GetMapping("/health")
    public ResponseEntity<Object> healthCheck() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", java.time.Instant.now().toString());
        health.put("service", "OEMS Backend API");
        return ResponseEntity.ok(health);
    }
    
    @GetMapping("/openapi")
    public ResponseEntity<String> openApiSpec() {
        String openApiSpec = """
        {
          "openapi": "3.0.1",
          "info": {
            "title": "OEMS API",
            "description": "Online Electric Mobility Services API",
            "version": "1.0.0"
          },
          "servers": [
            {
              "url": "http://localhost:8080",
              "description": "Development server"
            }
          ],
          "paths": {
            "/api/auth/send-otp": {
              "post": {
                "tags": ["Authentication"],
                "summary": "Send OTP verification code",
                "operationId": "sendOtp",
                "responses": {
                  "200": {
                    "description": "OTP sent successfully"
                  }
                }
              }
            },
            "/api/auth/verify-otp": {
              "post": {
                "tags": ["Authentication"],
                "summary": "Verify OTP code",
                "operationId": "verifyOtp",
                "responses": {
                  "200": {
                    "description": "OTP verified successfully"
                  }
                }
              }
            },
            "/api/auth/google": {
              "post": {
                "tags": ["Authentication"],
                "summary": "Google OAuth authentication",
                "operationId": "googleAuth",
                "responses": {
                  "200": {
                    "description": "Authentication successful"
                  }
                }
              }
            },
            "/api/rides/estimate": {
              "post": {
                "tags": ["Rides"],
                "summary": "Get ride cost estimates",
                "operationId": "getRideEstimate",
                "responses": {
                  "200": {
                    "description": "Ride estimates returned"
                  }
                }
              }
            },
            "/api/rides/book": {
              "post": {
                "tags": ["Rides"],
                "summary": "Book a ride",
                "operationId": "bookRide",
                "responses": {
                  "200": {
                    "description": "Ride booked successfully"
                  }
                }
              }
            },
            "/api/payments/charge": {
              "post": {
                "tags": ["Payments"],
                "summary": "Process payment",
                "operationId": "processPayment",
                "responses": {
                  "200": {
                    "description": "Payment processed successfully"
                  }
                }
              }
            }
          },
          "components": {
            "schemas": {}
          }
        }
        """;
        
        return ResponseEntity.ok()
            .header("Content-Type", "application/json")
            .body(openApiSpec);
    }
}