package com.example.backend.config;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.backend.entity.LogEntry;
import com.example.backend.repository.LogEntryRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
            try { logRepository.save(entry); } catch (Exception ignored) {}
        }
    }
}


