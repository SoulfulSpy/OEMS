package com.example.backend.oems.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Welcome controller to handle root path requests
 */
@Controller
public class WelcomeController {
    
    @GetMapping("/")
    public String welcome() {
        return "index.html";
    }
    
    @GetMapping("/api/welcome")
    @ResponseBody
    public String apiWelcome() {
        return "Welcome to OEMS API! Your application is running successfully.";
    }
}