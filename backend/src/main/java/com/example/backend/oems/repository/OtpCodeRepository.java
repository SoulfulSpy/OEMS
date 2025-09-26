package com.example.backend.oems.repository;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.oems.entity.OtpCode;

public interface OtpCodeRepository extends JpaRepository<OtpCode, Long> {
    Optional<OtpCode> findTopByPhoneAndExpiresAtAfterOrderByIdDesc(String phone, Instant now);
}


