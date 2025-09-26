-- Database initialization script for OEMS
-- This script will run when PostgreSQL container starts for the first time

-- Create database if not exists (handled by Docker environment variables)
-- CREATE DATABASE IF NOT EXISTS oems_db;

-- Create user if not exists (handled by Docker environment variables)
-- CREATE USER IF NOT EXISTS oems_user WITH PASSWORD 'oems_password';
-- GRANT ALL PRIVILEGES ON DATABASE oems_db TO oems_user;

-- Connect to the database
\c oems_db;

-- Enable UUID extension for primary keys
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create schema if not exists
CREATE SCHEMA IF NOT EXISTS public;

-- Grant permissions on schema
GRANT ALL ON SCHEMA public TO oems_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO oems_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO oems_user;

-- Set default privileges for future tables
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO oems_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO oems_user;

-- Log successful initialization
SELECT 'OEMS Database initialized successfully!' as status;