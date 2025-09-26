#!/bin/bash

# Local Development Runner for OEMS Spring Boot Application

echo "🚀 OEMS Local Development"
echo "========================"

# Check if PostgreSQL is running
if ! command -v pg_isready &> /dev/null; then
    echo "⚠️  PostgreSQL client not found. Install PostgreSQL client tools or use Docker."
fi

# Set local development environment variables
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/oems_db
export SPRING_DATASOURCE_USERNAME=oems_user
export SPRING_DATASOURCE_PASSWORD=oems_password
export JPA_DDL_AUTO=update
export JPA_SHOW_SQL=true
export JWT_SECRET=your-secret-key-change-this-in-production-environment-make-it-at-least-64-characters-long-for-security

echo "🔧 Environment configured for local development"
echo "📊 Database: postgresql://localhost:5432/oems_db"
echo "👤 Username: oems_user"

case "$1" in
    "run")
        echo "🚀 Starting Spring Boot application..."
        mvn spring-boot:run
        ;;
    "test")
        echo "🧪 Running tests..."
        mvn test
        ;;
    "clean")
        echo "🧹 Cleaning and compiling..."
        mvn clean compile
        ;;
    "package")
        echo "📦 Building package..."
        mvn clean package
        ;;
    *)
        echo "Usage: ./local-dev.sh [command]"
        echo ""
        echo "Commands:"
        echo "  run      - Start the Spring Boot application (mvn spring-boot:run)"
        echo "  test     - Run all tests"
        echo "  clean    - Clean and compile"
        echo "  package  - Build the JAR package"
        echo ""
        echo "Note: Make sure PostgreSQL is running on localhost:5432"
        echo "You can start the database with: ./scripts/oems.sh db-only"
        ;;
esac