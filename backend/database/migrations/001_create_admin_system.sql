-- =====================================================
-- OEMS Multi-Admin System Database Migration Script
-- Version: 1.0
-- Description: Creates new tables for enhanced admin system
-- =====================================================

-- Add new roles to user_roles table
ALTER TABLE user_roles MODIFY COLUMN role ENUM(
    'CUSTOMER', 
    'DRIVER', 
    'SHOWROOM_ADMIN', 
    'SUPER_ADMIN', 
    'DRIVER_ADMIN'
) NOT NULL;

-- =====================================================
-- Create showrooms table
-- =====================================================
CREATE TABLE showrooms (
    id BINARY(16) NOT NULL PRIMARY KEY,
    showroom_code VARCHAR(10) NOT NULL UNIQUE,
    showroom_name VARCHAR(255) NOT NULL,
    address VARCHAR(500) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    pincode VARCHAR(6),
    contact_phone VARCHAR(20),
    contact_email VARCHAR(255),
    latitude DECIMAL(10,8),
    longitude DECIMAL(11,8),
    status ENUM('ACTIVE', 'INACTIVE', 'MAINTENANCE', 'SUSPENDED') NOT NULL DEFAULT 'ACTIVE',
    max_drivers INT DEFAULT 50,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_showrooms_code (showroom_code),
    INDEX idx_showrooms_status (status),
    INDEX idx_showrooms_city (city),
    INDEX idx_showrooms_location (latitude, longitude)
);

-- =====================================================
-- Create showroom_admins table
-- =====================================================
CREATE TABLE showroom_admins (
    id BINARY(16) NOT NULL PRIMARY KEY,
    user_id BINARY(16) NOT NULL UNIQUE,
    showroom_id BINARY(16) NOT NULL,
    status ENUM('ACTIVE', 'INACTIVE', 'SUSPENDED', 'TRANSFERRED') NOT NULL DEFAULT 'ACTIVE',
    assigned_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Permission flags
    can_manage_drivers BOOLEAN NOT NULL DEFAULT TRUE,
    can_manage_vehicles BOOLEAN NOT NULL DEFAULT TRUE,
    can_view_analytics BOOLEAN NOT NULL DEFAULT TRUE,
    can_manage_rides BOOLEAN NOT NULL DEFAULT TRUE,
    
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (showroom_id) REFERENCES showrooms(id) ON DELETE CASCADE,
    
    INDEX idx_showroom_admins_user_id (user_id),
    INDEX idx_showroom_admins_showroom_id (showroom_id),
    INDEX idx_showroom_admins_status (status)
);

-- =====================================================
-- Create super_admins table
-- =====================================================
CREATE TABLE super_admins (
    id BINARY(16) NOT NULL PRIMARY KEY,
    user_id BINARY(16) NOT NULL UNIQUE,
    status ENUM('ACTIVE', 'INACTIVE', 'SUSPENDED') NOT NULL DEFAULT 'ACTIVE',
    designation VARCHAR(100),
    department VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL,
    
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    
    INDEX idx_super_admins_user_id (user_id),
    INDEX idx_super_admins_status (status),
    INDEX idx_super_admins_department (department)
);

-- =====================================================
-- Create super_admin_permissions table (for ElementCollection)
-- =====================================================
CREATE TABLE super_admin_permissions (
    super_admin_id BINARY(16) NOT NULL,
    permission ENUM(
        'MANAGE_USERS',
        'MANAGE_SHOWROOMS', 
        'MANAGE_ADMINS',
        'VIEW_ANALYTICS',
        'SYSTEM_CONFIG',
        'AUDIT_LOGS',
        'FINANCIAL_REPORTS',
        'PLATFORM_CONTROL'
    ) NOT NULL,
    
    PRIMARY KEY (super_admin_id, permission),
    FOREIGN KEY (super_admin_id) REFERENCES super_admins(id) ON DELETE CASCADE,
    
    INDEX idx_super_admin_permissions_admin_id (super_admin_id)
);

-- =====================================================
-- Create audit_logs table (for future audit functionality)
-- =====================================================
CREATE TABLE audit_logs (
    id BINARY(16) NOT NULL PRIMARY KEY,
    user_id BINARY(16),
    action VARCHAR(100) NOT NULL,
    entity_type VARCHAR(50) NOT NULL,
    entity_id BINARY(16),
    old_values JSON,
    new_values JSON,
    ip_address VARCHAR(45),
    user_agent TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL,
    
    INDEX idx_audit_logs_user_id (user_id),
    INDEX idx_audit_logs_entity (entity_type, entity_id),
    INDEX idx_audit_logs_action (action),
    INDEX idx_audit_logs_created_at (created_at)
);

-- =====================================================
-- Insert sample data for testing
-- =====================================================

-- Sample Showrooms
INSERT INTO showrooms (id, showroom_code, showroom_name, address, city, state, pincode, contact_phone, contact_email, latitude, longitude) VALUES
(UUID_TO_BIN(UUID()), 'KOL001', 'Kolkata Central Showroom', 'Park Street, Kolkata', 'Kolkata', 'West Bengal', '700016', '+919876543210', 'kolkata@naiyo24.com', 22.5726, 88.3639),
(UUID_TO_BIN(UUID()), 'DEL001', 'Delhi Main Showroom', 'Connaught Place, New Delhi', 'Delhi', 'Delhi', '110001', '+919876543211', 'delhi@naiyo24.com', 28.6315, 77.2167),
(UUID_TO_BIN(UUID()), 'MUM001', 'Mumbai Downtown Showroom', 'Bandra West, Mumbai', 'Mumbai', 'Maharashtra', '400050', '+919876543212', 'mumbai@naiyo24.com', 19.0596, 72.8295);

-- Sample Super Admin (You should replace with actual user IDs after creating users)
-- INSERT INTO super_admins (id, user_id, designation, department) VALUES
-- (UUID_TO_BIN(UUID()), UUID_TO_BIN('your-user-id-here'), 'CTO', 'Technology');

-- Sample permissions for super admin
-- INSERT INTO super_admin_permissions (super_admin_id, permission) 
-- SELECT id, 'MANAGE_USERS' FROM super_admins WHERE designation = 'CTO';
-- (Repeat for other permissions...)

-- =====================================================
-- Add useful views for common queries
-- =====================================================

-- View for showroom admin details with user information
CREATE VIEW showroom_admin_details AS
SELECT 
    sa.id as admin_id,
    sa.showroom_id,
    s.showroom_name,
    s.showroom_code,
    u.id as user_id,
    u.full_name,
    u.phone_number,
    u.email,
    sa.status as admin_status,
    sa.assigned_at,
    sa.can_manage_drivers,
    sa.can_manage_vehicles,
    sa.can_view_analytics,
    sa.can_manage_rides
FROM showroom_admins sa
JOIN users u ON sa.user_id = u.id
JOIN showrooms s ON sa.showroom_id = s.id;

-- View for super admin details with user information  
CREATE VIEW super_admin_details AS
SELECT 
    sa.id as admin_id,
    u.id as user_id,
    u.full_name,
    u.phone_number,
    u.email,
    sa.designation,
    sa.department,
    sa.status as admin_status,
    sa.created_at,
    sa.last_login,
    GROUP_CONCAT(sap.permission) as permissions
FROM super_admins sa
JOIN users u ON sa.user_id = u.id
LEFT JOIN super_admin_permissions sap ON sa.id = sap.super_admin_id
GROUP BY sa.id, u.id, u.full_name, u.phone_number, u.email, sa.designation, sa.department, sa.status, sa.created_at, sa.last_login;

-- =====================================================
-- Performance optimization indexes
-- =====================================================

-- Additional indexes for common query patterns
CREATE INDEX idx_users_roles_composite ON user_roles(user_id, role);
CREATE INDEX idx_showrooms_status_city ON showrooms(status, city);
CREATE INDEX idx_showroom_admins_permissions ON showroom_admins(can_manage_drivers, can_manage_vehicles, can_view_analytics, can_manage_rides);

-- =====================================================
-- Migration completion verification
-- =====================================================

-- Verify table creation
SELECT 
    TABLE_NAME, 
    TABLE_ROWS, 
    CREATE_TIME
FROM INFORMATION_SCHEMA.TABLES 
WHERE TABLE_SCHEMA = DATABASE() 
AND TABLE_NAME IN ('showrooms', 'showroom_admins', 'super_admins', 'super_admin_permissions', 'audit_logs');

-- Verify indexes
SELECT 
    TABLE_NAME, 
    INDEX_NAME, 
    COLUMN_NAME
FROM INFORMATION_SCHEMA.STATISTICS 
WHERE TABLE_SCHEMA = DATABASE() 
AND TABLE_NAME IN ('showrooms', 'showroom_admins', 'super_admins')
ORDER BY TABLE_NAME, INDEX_NAME, SEQ_IN_INDEX;

COMMIT;