package com.example.backend.oems.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.backend.oems.entity.SuperAdmin;
import com.example.backend.oems.entity.User;

/**
 * Repository interface for SuperAdmin entity operations
 */
@Repository
public interface SuperAdminRepository extends JpaRepository<SuperAdmin, UUID> {

    /**
     * Find super admin by user
     */
    Optional<SuperAdmin> findByUser(User user);

    /**
     * Find super admin by user ID
     */
    Optional<SuperAdmin> findByUserId(UUID userId);

    /**
     * Find all super admins by status
     */
    List<SuperAdmin> findByStatus(SuperAdmin.SuperAdminStatus status);

    /**
     * Find all active super admins
     */
    @Query("SELECT sa FROM SuperAdmin sa WHERE sa.status = 'ACTIVE'")
    List<SuperAdmin> findAllActiveAdmins();

    /**
     * Find super admins by department
     */
    List<SuperAdmin> findByDepartmentIgnoreCase(String department);

    /**
     * Find super admins by designation
     */
    List<SuperAdmin> findByDesignationIgnoreCase(String designation);

    /**
     * Check if user is already a super admin
     */
    boolean existsByUser(User user);

    /**
     * Check if user is already a super admin by user ID
     */
    boolean existsByUserId(UUID userId);

    /**
     * Count super admins by status
     */
    long countByStatus(SuperAdmin.SuperAdminStatus status);

    /**
     * Count total active super admins
     */
    @Query("SELECT COUNT(sa) FROM SuperAdmin sa WHERE sa.status = 'ACTIVE'")
    long countActiveAdmins();

    /**
     * Find super admins with specific permission
     */
    @Query("SELECT sa FROM SuperAdmin sa WHERE sa.status = 'ACTIVE' AND :permission MEMBER OF sa.permissions")
    List<SuperAdmin> findAdminsWithPermission(@Param("permission") SuperAdmin.SuperAdminPermission permission);

    /**
     * Find super admins with all critical permissions (for emergency operations)
     */
    @Query("""
        SELECT sa FROM SuperAdmin sa WHERE sa.status = 'ACTIVE' 
        AND 'PLATFORM_CONTROL' MEMBER OF sa.permissions 
        AND 'SYSTEM_CONFIG' MEMBER OF sa.permissions
        """)
    List<SuperAdmin> findEmergencyAdmins();

    /**
     * Find recently logged in super admins (last 30 days)
     */
    @Query(value = "SELECT * FROM super_admins WHERE status = 'ACTIVE' " +
           "AND last_login IS NOT NULL " +
           "AND last_login >= CURRENT_TIMESTAMP - INTERVAL '30 days'", 
           nativeQuery = true)
    List<SuperAdmin> findRecentlyActiveAdmins();

    /**
     * Find super admins by department with specific permissions
     */
    @Query("SELECT sa FROM SuperAdmin sa WHERE sa.status = 'ACTIVE' " +
           "AND LOWER(sa.department) = LOWER(:department) " +
           "AND :permission MEMBER OF sa.permissions")
    List<SuperAdmin> findAdminsByDepartmentWithPermission(@Param("department") String department,
                                                         @Param("permission") SuperAdmin.SuperAdminPermission permission);

    /**
     * Get super admin statistics by department
     */
    @Query("SELECT sa.department, COUNT(sa) FROM SuperAdmin sa WHERE sa.status = 'ACTIVE' GROUP BY sa.department")
    List<Object[]> getAdminCountByDepartment();
}