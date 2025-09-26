package com.example.backend.oems.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @PostMapping("/charge")
    public ResponseEntity<Object> charge(@RequestBody Map<String, Object> body, @RequestHeader(value = "X-User-Phone", required = false) String phone) {
        // Dummy success response for now
        return ResponseEntity.ok(Map.of(
                "status", "succeeded",
                "amount", body.getOrDefault("amount", 0),
                "currency", body.getOrDefault("currency", "INR"),
                "paymentMethod", body.getOrDefault("paymentMethod", "card")
        ));
    }
}


