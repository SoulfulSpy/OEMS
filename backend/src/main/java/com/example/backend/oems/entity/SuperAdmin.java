package com.example.backend.oems.entity;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

/**
 * SuperAdmin entity for platform-wide administration (Naiyo24 company)
 * Has highest level of system access and control
 */
@Entity
@Table(name = "super_admins", indexes = {
    @Index(name = "idx_super_admins_user_id", columnList = "user_id", unique = true),
    @Index(name = "idx_super_admins_status", columnList = "status")
})
public class SuperAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SuperAdminStatus status = SuperAdminStatus.ACTIVE;

    @Column(name = "designation")
    private String designation; // CEO, CTO, Operations Head, etc.

    @Column(name = "department")
    private String department; // Technology, Operations, Business, etc.

    @ElementCollection(targetClass = SuperAdminPermission.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @Column(name = "permission")
    private Set<SuperAdminPermission> permissions = new HashSet<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    @Column(name = "last_login")
    private Instant lastLogin;

    // Constructors
    public SuperAdmin() {}

    public SuperAdmin(User user, String designation, String department) {
        this.user = user;
        this.designation = designation;
        this.department = department;
        // Ensure user has SUPER_ADMIN role
        if (!user.hasRole(User.UserRole.SUPER_ADMIN)) {
            user.addRole(User.UserRole.SUPER_ADMIN);
        }
        // Grant all permissions by default
        initializeAllPermissions();
    }

    private void initializeAllPermissions() {
        permissions.clear();
        permissions.add(SuperAdminPermission.MANAGE_USERS);
        permissions.add(SuperAdminPermission.MANAGE_SHOWROOMS);
        permissions.add(SuperAdminPermission.MANAGE_ADMINS);
        permissions.add(SuperAdminPermission.VIEW_ANALYTICS);
        permissions.add(SuperAdminPermission.SYSTEM_CONFIG);
        permissions.add(SuperAdminPermission.AUDIT_LOGS);
        permissions.add(SuperAdminPermission.FINANCIAL_REPORTS);
        permissions.add(SuperAdminPermission.PLATFORM_CONTROL);
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public SuperAdminStatus getStatus() { return status; }
    public void setStatus(SuperAdminStatus status) { this.status = status; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public Set<SuperAdminPermission> getPermissions() { return permissions; }
    public void setPermissions(Set<SuperAdminPermission> permissions) { this.permissions = permissions; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public Instant getLastLogin() { return lastLogin; }
    public void setLastLogin(Instant lastLogin) { this.lastLogin = lastLogin; }

    // Helper methods
    public boolean hasPermission(SuperAdminPermission permission) {
        return permissions.contains(permission);
    }

    public void addPermission(SuperAdminPermission permission) {
        this.permissions.add(permission);
    }

    public void removePermission(SuperAdminPermission permission) {
        this.permissions.remove(permission);
    }

    public void grantAllPermissions() {
        permissions.clear();
        permissions.add(SuperAdminPermission.MANAGE_USERS);
        permissions.add(SuperAdminPermission.MANAGE_SHOWROOMS);
        permissions.add(SuperAdminPermission.MANAGE_ADMINS);
        permissions.add(SuperAdminPermission.VIEW_ANALYTICS);
        permissions.add(SuperAdminPermission.SYSTEM_CONFIG);
        permissions.add(SuperAdminPermission.AUDIT_LOGS);
        permissions.add(SuperAdminPermission.FINANCIAL_REPORTS);
        permissions.add(SuperAdminPermission.PLATFORM_CONTROL);
    }

    public void updateLastLogin() {
        this.lastLogin = Instant.now();
    }

    // JPA Callbacks
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    /**
     * Super admin status enumeration
     */
    public enum SuperAdminStatus {
        ACTIVE,     // Currently active
        INACTIVE,   // Temporarily inactive
        SUSPENDED   // Suspended (emergency only)
    }

    /**
     * Super admin permission enumeration
     */
    public enum SuperAdminPermission {
        MANAGE_USERS,       // Create/update/delete all users
        MANAGE_SHOWROOMS,   // Create/update/delete showrooms
        MANAGE_ADMINS,      // Create/update/delete all admin accounts
        VIEW_ANALYTICS,     // View platform-wide analytics
        SYSTEM_CONFIG,      // System configuration changes
        AUDIT_LOGS,         // View audit logs and system logs
        FINANCIAL_REPORTS,  // Access financial reports and data
        PLATFORM_CONTROL    // Emergency platform controls (suspend/activate)
    }
}