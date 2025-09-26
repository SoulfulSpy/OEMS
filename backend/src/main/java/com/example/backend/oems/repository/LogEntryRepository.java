package com.example.backend.oems.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.oems.entity.LogEntry;

public interface LogEntryRepository extends JpaRepository<LogEntry, UUID> {}


