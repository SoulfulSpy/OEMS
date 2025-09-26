#!/bin/bash

# Local Development Runner for OEMS Spring Boot Application

echo "🚀 OEMS Local Development"
echo "========================"

# Function to check database connectivity
check_database() {
    echo "🔍 Checking database connectivity..."
    
    # Check if PostgreSQL is accessible
    if command -v pg_isready &> /dev/null; then
        if pg_isready -h localhost -p 5432 -U oems_user > /dev/null 2>&1; then
            echo "✅ PostgreSQL is running and accessible"
            return 0
        else
            echo "❌ PostgreSQL is not accessible on localhost:5432"
            return 1
        fi
    else
        # Try with Docker
        if docker-compose exec -T postgres pg_isready -U oems_user > /dev/null 2>&1; then
            echo "✅ PostgreSQL is running in Docker"
            return 0
        else
            echo "❌ PostgreSQL is not running"
            echo "💡 Start with: ./scripts/oems.sh dev"
            return 1
        fi
    fi
}

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
        if ! check_database; then
            exit 1
        fi
        
        echo "🚀 Starting Spring Boot application..."
        echo "🌐 Application will be available at: http://localhost:8080"
        echo "📚 API Documentation: http://localhost:8080/swagger-ui.html"
        echo ""
        echo "Press Ctrl+C to stop the application"
        echo "======================================"
        mvn spring-boot:run
        ;;
    "test")
        echo "🧪 Running tests..."
        echo "ℹ️  Tests use H2 in-memory database (no external DB required)"
        mvn test
        
        if [ $? -eq 0 ]; then
            echo "✅ All tests passed!"
        else
            echo "❌ Some tests failed"
            exit 1
        fi
        ;;
    "clean")
        echo "🧹 Cleaning and compiling..."
        mvn clean compile
        ;;
    "package")
        echo "📦 Building package..."
        mvn clean package -DskipTests
        
        if [ $? -eq 0 ]; then
            echo "✅ Package built successfully!"
            echo "📦 JAR location: target/backend-0.0.1-SNAPSHOT.jar"
            echo "🚀 Run with: java -jar target/backend-0.0.1-SNAPSHOT.jar"
        else
            echo "❌ Package build failed"
            exit 1
        fi
        ;;
    "dev")
        echo "🚀 Starting OEMS in Development Mode..."
        
        # Start database if not running
        if ! check_database; then
            echo "🗄️  Starting database services..."
            ../scripts/oems.sh dev
            sleep 3
        fi
        
        echo "🔥 Starting with hot-reload (spring-boot-devtools)"
        echo "🌐 Application: http://localhost:8080"
        echo "📚 API Docs: http://localhost:8080/swagger-ui.html"
        echo "🔄 Auto-restart on file changes"
        echo ""
        echo "Press Ctrl+C to stop"
        echo "===================="
        mvn spring-boot:run -Dspring-boot.run.addResources=true
        ;;
    *)
        echo "Usage: ./local-dev.sh [command]"
        echo ""
        echo "Commands:"
        echo "  run      - Start the Spring Boot application (mvn spring-boot:run)"
        echo "  dev      - Start in development mode with auto-restart"
        echo "  test     - Run all tests"
        echo "  clean    - Clean and compile"
        echo "  package  - Build the JAR package"
        echo ""
        echo ""
        echo "💡 Database requirement:"
        echo "   Make sure PostgreSQL is running on localhost:5432"
        echo "   Start with: ./scripts/oems.sh dev"
        echo ""
        echo "🚀 Quick start: ./scripts/local-dev.sh dev"
        ;;
esac