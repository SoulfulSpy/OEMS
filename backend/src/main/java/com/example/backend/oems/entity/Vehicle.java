package com.example.backend.oems.entity;

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
 * Vehicle entity for managing vehicle information and driver-vehicle relationships
 * Based on the Red Tables section of the ERD and Driver Service specifications
 */
@Entity
@Table(name = "vehicles", indexes = {
    @Index(name = "idx_vehicles_driver_id", columnList = "driverId"),
    @Index(name = "idx_vehicles_license_plate", columnList = "licensePlate", unique = true),
    @Index(name = "idx_vehicles_vehicle_type", columnList = "vehicleType"),
    @Index(name = "idx_vehicles_status", columnList = "status")
})
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "driver_id")
    private UUID driverId;

    @Column(name = "license_plate", nullable = false, unique = true, length = 20)
    private String licensePlate;

    @Column(nullable = false, length = 50)
    private String make;

    @Column(nullable = false, length = 50)
    private String model;

    @Column(nullable = false)
    private Integer year;

    @Column(length = 30)
    private String color;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", nullable = false)
    private VehicleType vehicleType;

    @Column(name = "seating_capacity", nullable = false)
    private Integer seatingCapacity;

    @Column(name = "fuel_type", length = 20)
    private String fuelType;

    @Column(name = "insurance_number", length = 50)
    private String insuranceNumber;

    @Column(name = "registration_number", length = 50)
    private String registrationNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleStatus status = VehicleStatus.ACTIVE;

    @Column(name = "is_verified")
    private Boolean isVerified = false;

    @Column(name = "verification_date")
    private Instant verificationDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    // Constructors
    public Vehicle() {}

    public Vehicle(String licensePlate, String make, String model, Integer year, 
                   VehicleType vehicleType, Integer seatingCapacity) {
        this.licensePlate = licensePlate;
        this.make = make;
        this.model = model;
        this.year = year;
        this.vehicleType = vehicleType;
        this.seatingCapacity = seatingCapacity;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getDriverId() { return driverId; }
    public void setDriverId(UUID driverId) { this.driverId = driverId; }

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    public String getMake() { return make; }
    public void setMake(String make) { this.make = make; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public VehicleType getVehicleType() { return vehicleType; }
    public void setVehicleType(VehicleType vehicleType) { this.vehicleType = vehicleType; }

    public Integer getSeatingCapacity() { return seatingCapacity; }
    public void setSeatingCapacity(Integer seatingCapacity) { this.seatingCapacity = seatingCapacity; }

    public String getFuelType() { return fuelType; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }

    public String getInsuranceNumber() { return insuranceNumber; }
    public void setInsuranceNumber(String insuranceNumber) { this.insuranceNumber = insuranceNumber; }

    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }

    public VehicleStatus getStatus() { return status; }
    public void setStatus(VehicleStatus status) { this.status = status; }

    public Boolean getIsVerified() { return isVerified; }
    public void setIsVerified(Boolean isVerified) { this.isVerified = isVerified; }

    public Instant getVerificationDate() { return verificationDate; }
    public void setVerificationDate(Instant verificationDate) { this.verificationDate = verificationDate; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    // Helper methods
    public void verify() {
        this.isVerified = true;
        this.verificationDate = Instant.now();
        this.status = VehicleStatus.ACTIVE;
    }

    public void deactivate() {
        this.status = VehicleStatus.INACTIVE;
    }

    public boolean isAvailableForService() {
        return status == VehicleStatus.ACTIVE && isVerified;
    }

    public String getDisplayName() {
        return String.format("%s %s %d (%s)", make, model, year, licensePlate);
    }

    // JPA Callbacks
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    /**
     * Vehicle types supported by the ride-hailing platform
     */
    public enum VehicleType {
        SEDAN,
        HATCHBACK,
        SUV,
        AUTO_RICKSHAW,
        BIKE,
        LUXURY,
        PREMIUM
    }

    /**
     * Vehicle status for managing vehicle lifecycle
     */
    public enum VehicleStatus {
        ACTIVE,
        INACTIVE,
        MAINTENANCE,
        SUSPENDED
    }
}