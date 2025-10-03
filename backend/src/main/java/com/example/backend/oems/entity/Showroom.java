package com.example.backend.oems.entity;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Showroom entity representing physical showroom locations
 * Each showroom has its own admin and manages a fleet of drivers/vehicles
 */
@Entity
@Table(name = "showrooms", indexes = {
    @Index(name = "idx_showrooms_code", columnList = "showroomCode", unique = true),
    @Index(name = "idx_showrooms_status", columnList = "status"),
    @Index(name = "idx_showrooms_city", columnList = "city")
})
public class Showroom {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Showroom code is required")
    @Pattern(regexp = "^[A-Z]{2,4}[0-9]{3}$", message = "Invalid showroom code format (e.g., KOL001)")
    @Column(name = "showroom_code", nullable = false, unique = true, length = 10)
    private String showroomCode;

    @NotBlank(message = "Showroom name is required")
    @Column(name = "showroom_name", nullable = false)
    private String showroomName;

    @NotBlank(message = "Address is required")
    @Column(nullable = false, length = 500)
    private String address;

    @NotBlank(message = "City is required")
    @Column(nullable = false)
    private String city;

    @NotBlank(message = "State is required")
    @Column(nullable = false)
    private String state;

    @Pattern(regexp = "^[0-9]{6}$", message = "Invalid pincode format")
    @Column(length = 6)
    private String pincode;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
    @Column(name = "contact_phone", length = 20)
    private String contactPhone;

    @Column(name = "contact_email")
    private String contactEmail;

    // Geographic coordinates for location-based services
    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShowroomStatus status = ShowroomStatus.ACTIVE;

    @Column(name = "max_drivers")
    private Integer maxDrivers = 50; // Default capacity

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    // Relationships
    @OneToMany(mappedBy = "showroom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ShowroomAdmin> admins = new HashSet<>();

    // TODO: Add Driver relationship when Driver entity is enhanced
    // @OneToMany(mappedBy = "showroom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // private Set<Driver> drivers = new HashSet<>();

    // Constructors
    public Showroom() {}

    public Showroom(String showroomCode, String showroomName, String address, String city, String state) {
        this.showroomCode = showroomCode;
        this.showroomName = showroomName; 
        this.address = address;
        this.city = city;
        this.state = state;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getShowroomCode() { return showroomCode; }
    public void setShowroomCode(String showroomCode) { this.showroomCode = showroomCode; }

    public String getShowroomName() { return showroomName; }
    public void setShowroomName(String showroomName) { this.showroomName = showroomName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }

    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }

    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public ShowroomStatus getStatus() { return status; }
    public void setStatus(ShowroomStatus status) { this.status = status; }

    public Integer getMaxDrivers() { return maxDrivers; }
    public void setMaxDrivers(Integer maxDrivers) { this.maxDrivers = maxDrivers; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public Set<ShowroomAdmin> getAdmins() { return admins; }
    public void setAdmins(Set<ShowroomAdmin> admins) { this.admins = admins; }

    // Helper methods
    public void addAdmin(ShowroomAdmin admin) {
        admins.add(admin);
        admin.setShowroom(this);
    }

    public void removeAdmin(ShowroomAdmin admin) {
        admins.remove(admin);
        admin.setShowroom(null);
    }

    // JPA Callbacks
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    /**
     * Showroom status enumeration
     */
    public enum ShowroomStatus {
        ACTIVE,      // Operational
        INACTIVE,    // Temporarily closed
        MAINTENANCE, // Under maintenance
        SUSPENDED    // Suspended by super admin
    }
}