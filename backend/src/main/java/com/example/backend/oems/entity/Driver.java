package com.example.backend.oems.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

/**
 * Driver entity for managing driver-specific information
 * Based on the Red Tables section of the ERD and Driver Service specifications
 */
@Entity
@Table(name = "drivers", indexes = {
    @Index(name = "idx_drivers_user_id", columnList = "userId", unique = true),
    @Index(name = "idx_drivers_license_number", columnList = "licenseNumber", unique = true),
    @Index(name = "idx_drivers_status", columnList = "onboardingStatus")
})
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false, unique = true)
    private UUID userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @Column(name = "license_number", nullable = false, unique = true, length = 50)
    private String licenseNumber;

    @Column(name = "vehicle_id")
    private UUID vehicleId;

    @Enumerated(EnumType.STRING)
    @Column(name = "onboarding_status", nullable = false)
    private OnboardingStatus onboardingStatus = OnboardingStatus.PENDING;

    @Column(name = "average_rating", precision = 3, scale = 2)
    private BigDecimal averageRating = BigDecimal.valueOf(5.00);

    @Column(name = "total_ratings")
    private Integer totalRatings = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_status")
    private DriverStatus currentStatus = DriverStatus.OFFLINE;

    @Column(name = "is_available")
    private Boolean isAvailable = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    // Constructors
    public Driver() {}

    public Driver(UUID userId, String name, String email, String phoneNumber, String licenseNumber) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.licenseNumber = licenseNumber;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    public UUID getVehicleId() { return vehicleId; }
    public void setVehicleId(UUID vehicleId) { this.vehicleId = vehicleId; }

    public OnboardingStatus getOnboardingStatus() { return onboardingStatus; }
    public void setOnboardingStatus(OnboardingStatus onboardingStatus) { this.onboardingStatus = onboardingStatus; }

    public BigDecimal getAverageRating() { return averageRating; }
    public void setAverageRating(BigDecimal averageRating) { this.averageRating = averageRating; }

    public Integer getTotalRatings() { return totalRatings; }
    public void setTotalRatings(Integer totalRatings) { this.totalRatings = totalRatings; }

    public DriverStatus getCurrentStatus() { return currentStatus; }
    public void setCurrentStatus(DriverStatus currentStatus) { this.currentStatus = currentStatus; }

    public Boolean getIsAvailable() { return isAvailable; }
    public void setIsAvailable(Boolean isAvailable) { this.isAvailable = isAvailable; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    // Helper methods
    public void updateRating(BigDecimal newRating) {
        if (this.totalRatings == 0) {
            this.averageRating = newRating;
            this.totalRatings = 1;
        } else {
            BigDecimal totalScore = this.averageRating.multiply(BigDecimal.valueOf(this.totalRatings));
            totalScore = totalScore.add(newRating);
            this.totalRatings++;
            this.averageRating = totalScore.divide(BigDecimal.valueOf(this.totalRatings), 2, RoundingMode.HALF_UP);
        }
    }

    public boolean isOnline() {
        return currentStatus == DriverStatus.ONLINE && isAvailable;
    }

    // JPA Callbacks
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    /**
     * Driver onboarding status based on Driver Service specifications
     */
    public enum OnboardingStatus {
        PENDING,
        DOCUMENTS_SUBMITTED,
        APPROVED,
        REJECTED
    }

    /**
     * Driver current status for real-time availability
     */
    public enum DriverStatus {
        OFFLINE,
        ONLINE,
        ON_TRIP,
        BUSY
    }
}