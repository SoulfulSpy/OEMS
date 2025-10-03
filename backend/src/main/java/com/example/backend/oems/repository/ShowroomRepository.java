package com.example.backend.oems.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.backend.oems.entity.Showroom;

/**
 * Repository interface for Showroom entity operations
 */
@Repository
public interface ShowroomRepository extends JpaRepository<Showroom, UUID> {

    /**
     * Find showroom by unique showroom code
     */
    Optional<Showroom> findByShowroomCode(String showroomCode);

    /**
     * Find all showrooms by city
     */
    List<Showroom> findByCityIgnoreCase(String city);

    /**
     * Find all showrooms by state
     */
    List<Showroom> findByStateIgnoreCase(String state);

    /**
     * Find all showrooms by status
     */
    List<Showroom> findByStatus(Showroom.ShowroomStatus status);

    /**
     * Find all active showrooms
     */
    @Query("SELECT s FROM Showroom s WHERE s.status = 'ACTIVE'")
    List<Showroom> findAllActiveShowrooms();

    /**
     * Find showrooms within a geographic radius (for location-based queries)
     * Using Haversine formula for distance calculation
     */
    @Query(value = """
        SELECT * FROM showrooms s 
        WHERE s.latitude IS NOT NULL AND s.longitude IS NOT NULL
        AND s.status = 'ACTIVE'
        AND (6371 * acos(cos(radians(:lat)) * cos(radians(s.latitude)) * 
             cos(radians(s.longitude) - radians(:lng)) + sin(radians(:lat)) * 
             sin(radians(s.latitude)))) <= :radiusKm
        ORDER BY (6371 * acos(cos(radians(:lat)) * cos(radians(s.latitude)) * 
                 cos(radians(s.longitude) - radians(:lng)) + sin(radians(:lat)) * 
                 sin(radians(s.latitude))))
        """, nativeQuery = true)
    List<Showroom> findShowroomsWithinRadius(@Param("lat") double latitude, 
                                           @Param("lng") double longitude, 
                                           @Param("radiusKm") double radiusKm);

    /**
     * Count total showrooms by status
     */
    long countByStatus(Showroom.ShowroomStatus status);

    /**
     * Find showrooms by partial name match (case-insensitive)
     */
    @Query("SELECT s FROM Showroom s WHERE LOWER(s.showroomName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Showroom> findByShowroomNameContaining(@Param("name") String name);

    /**
     * Check if showroom code already exists
     */
    boolean existsByShowroomCode(String showroomCode);

    /**
     * Get showrooms with driver count (for capacity management)
     * TODO: Update this query when Driver entity is enhanced with showroom relationship
     */
    @Query("""
        SELECT s, 
               (SELECT COUNT(sa) FROM ShowroomAdmin sa WHERE sa.showroom.id = s.id AND sa.status = 'ACTIVE') as adminCount
        FROM Showroom s 
        WHERE s.status = 'ACTIVE'
        """)
    List<Object[]> findShowroomsWithStats();
}