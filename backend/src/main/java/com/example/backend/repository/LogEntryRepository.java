package com.example.backend.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.entity.LogEntry;

public interface LogEntryRepository extends JpaRepository<LogEntry, UUID> {}


