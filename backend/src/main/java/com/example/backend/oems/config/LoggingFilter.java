package com.example.backend.oems.config;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.backend.oems.entity.LogEntry;
import com.example.backend.oems.repository.LogEntryRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Request logging filter that captures HTTP request details and stores them in the database.
 * This filter logs method, path, user phone, status code, and response time for each request.
 */
@Component
public class LoggingFilter extends OncePerRequestFilter {
    
    private final LogEntryRepository logRepository;

    public LoggingFilter(LogEntryRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        long start = System.currentTimeMillis();
        try {
            filterChain.doFilter(request, response);
        } finally {
            LogEntry entry = new LogEntry();
            entry.setMethod(request.getMethod());
            entry.setPath(request.getRequestURI());
            entry.setUserPhone(request.getHeader("X-User-Phone"));
            entry.setStatus(response.getStatus());
            entry.setMetadata("durationMs=" + (System.currentTimeMillis() - start));
            
            // Save log entry, ignore any database errors to prevent request failure
            try { 
                logRepository.save(entry); 
            } catch (Exception ignored) {
                // Logging should not break the application
            }
        }
    }
}