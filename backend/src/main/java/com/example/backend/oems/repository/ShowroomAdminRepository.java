package com.example.backend.oems.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.backend.oems.entity.Showroom;
import com.example.backend.oems.entity.ShowroomAdmin;
import com.example.backend.oems.entity.User;

/**
 * Repository interface for ShowroomAdmin entity operations
 */
@Repository
public interface ShowroomAdminRepository extends JpaRepository<ShowroomAdmin, UUID> {

    /**
     * Find showroom admin by user
     */
    Optional<ShowroomAdmin> findByUser(User user);

    /**
     * Find showroom admin by user ID
     */
    Optional<ShowroomAdmin> findByUserId(UUID userId);

    /**
     * Find all admins for a specific showroom
     */
    List<ShowroomAdmin> findByShowroom(Showroom showroom);

    /**
     * Find all admins for a specific showroom by showroom ID
     */
    List<ShowroomAdmin> findByShowroomId(UUID showroomId);

    /**
     * Find all admins by status
     */
    List<ShowroomAdmin> findByStatus(ShowroomAdmin.AdminStatus status);

    /**
     * Find all active admins for a showroom
     */
    @Query("SELECT sa FROM ShowroomAdmin sa WHERE sa.showroom.id = :showroomId AND sa.status = 'ACTIVE'")
    List<ShowroomAdmin> findActiveAdminsByShowroomId(@Param("showroomId") UUID showroomId);

    /**
     * Find all active showroom admins
     */
    @Query("SELECT sa FROM ShowroomAdmin sa WHERE sa.status = 'ACTIVE'")
    List<ShowroomAdmin> findAllActiveAdmins();

    /**
     * Check if user is already a showroom admin
     */
    boolean existsByUser(User user);

    /**
     * Check if user is already a showroom admin by user ID
     */
    boolean existsByUserId(UUID userId);

    /**
     * Count admins by showroom
     */
    long countByShowroom(Showroom showroom);

    /**
     * Count active admins by showroom
     */
    @Query("SELECT COUNT(sa) FROM ShowroomAdmin sa WHERE sa.showroom.id = :showroomId AND sa.status = 'ACTIVE'")
    long countActiveAdminsByShowroomId(@Param("showroomId") UUID showroomId);

    /**
     * Find admins with specific permissions
     */
    @Query("SELECT sa FROM ShowroomAdmin sa WHERE sa.status = 'ACTIVE' AND " +
           "(:canManageDrivers IS NULL OR sa.canManageDrivers = :canManageDrivers) AND " +
           "(:canManageVehicles IS NULL OR sa.canManageVehicles = :canManageVehicles) AND " +
           "(:canViewAnalytics IS NULL OR sa.canViewAnalytics = :canViewAnalytics) AND " +
           "(:canManageRides IS NULL OR sa.canManageRides = :canManageRides)")
    List<ShowroomAdmin> findAdminsByPermissions(@Param("canManageDrivers") Boolean canManageDrivers,
                                              @Param("canManageVehicles") Boolean canManageVehicles,
                                              @Param("canViewAnalytics") Boolean canViewAnalytics,
                                              @Param("canManageRides") Boolean canManageRides);

    /**
     * Find showroom admin with full permissions by showroom
     */
    @Query("SELECT sa FROM ShowroomAdmin sa WHERE sa.showroom.id = :showroomId AND sa.status = 'ACTIVE' " +
           "AND sa.canManageDrivers = true AND sa.canManageVehicles = true " +
           "AND sa.canViewAnalytics = true AND sa.canManageRides = true")
    List<ShowroomAdmin> findFullPermissionAdminsByShowroomId(@Param("showroomId") UUID showroomId);

    /**
     * Find showroom admins by city (through showroom relationship)
     */
    @Query("SELECT sa FROM ShowroomAdmin sa WHERE sa.showroom.city = :city AND sa.status = 'ACTIVE'")
    List<ShowroomAdmin> findAdminsByCity(@Param("city") String city);
}