package com.example.backend.oems.entity;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

/**
 * ShowroomAdmin entity linking Users with SHOWROOM_ADMIN role to specific showrooms
 * Represents the admin management structure for showroom operations
 */
@Entity
@Table(name = "showroom_admins", indexes = {
    @Index(name = "idx_showroom_admins_user_id", columnList = "user_id", unique = true),
    @Index(name = "idx_showroom_admins_showroom_id", columnList = "showroom_id"),
    @Index(name = "idx_showroom_admins_status", columnList = "status")
})
public class ShowroomAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showroom_id", nullable = false)
    private Showroom showroom;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdminStatus status = AdminStatus.ACTIVE;

    @Column(name = "assigned_at", nullable = false, updatable = false)
    private Instant assignedAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    // Admin permissions (can be expanded later)
    @Column(name = "can_manage_drivers", nullable = false)
    private Boolean canManageDrivers = true;

    @Column(name = "can_manage_vehicles", nullable = false)
    private Boolean canManageVehicles = true;

    @Column(name = "can_view_analytics", nullable = false)
    private Boolean canViewAnalytics = true;

    @Column(name = "can_manage_rides", nullable = false)
    private Boolean canManageRides = true;

    // Constructors
    public ShowroomAdmin() {}

    public ShowroomAdmin(User user, Showroom showroom) {
        this.user = user;
        this.showroom = showroom;
        // Ensure user has SHOWROOM_ADMIN role
        if (!user.hasRole(User.UserRole.SHOWROOM_ADMIN)) {
            user.addRole(User.UserRole.SHOWROOM_ADMIN);
        }
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Showroom getShowroom() { return showroom; }
    public void setShowroom(Showroom showroom) { this.showroom = showroom; }

    public AdminStatus getStatus() { return status; }
    public void setStatus(AdminStatus status) { this.status = status; }

    public Instant getAssignedAt() { return assignedAt; }
    public void setAssignedAt(Instant assignedAt) { this.assignedAt = assignedAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public Boolean getCanManageDrivers() { return canManageDrivers; }
    public void setCanManageDrivers(Boolean canManageDrivers) { this.canManageDrivers = canManageDrivers; }

    public Boolean getCanManageVehicles() { return canManageVehicles; }
    public void setCanManageVehicles(Boolean canManageVehicles) { this.canManageVehicles = canManageVehicles; }

    public Boolean getCanViewAnalytics() { return canViewAnalytics; }
    public void setCanViewAnalytics(Boolean canViewAnalytics) { this.canViewAnalytics = canViewAnalytics; }

    public Boolean getCanManageRides() { return canManageRides; }
    public void setCanManageRides(Boolean canManageRides) { this.canManageRides = canManageRides; }

    // Helper methods
    public boolean hasFullPermissions() {
        return canManageDrivers && canManageVehicles && canViewAnalytics && canManageRides;
    }

    public void grantAllPermissions() {
        this.canManageDrivers = true;
        this.canManageVehicles = true;
        this.canViewAnalytics = true;
        this.canManageRides = true;
    }

    public void revokeAllPermissions() {
        this.canManageDrivers = false;
        this.canManageVehicles = false;
        this.canViewAnalytics = false;
        this.canManageRides = false;
    }

    // JPA Callbacks
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    /**
     * Admin status enumeration
     */
    public enum AdminStatus {
        ACTIVE,     // Currently managing showroom
        INACTIVE,   // Temporarily inactive
        SUSPENDED,  // Suspended by super admin
        TRANSFERRED // Moved to different showroom
    }
}