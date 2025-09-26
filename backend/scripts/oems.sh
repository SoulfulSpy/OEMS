#!/bin/bash

# OEMS Docker Environment Management Script

echo "🚀 OEMS Docker Environment Manager"
echo "=================================="

case "$1" in
    "start")
        echo "Starting OEMS services..."
        
        # Check if Docker is running
        if ! docker info > /dev/null 2>&1; then
            echo "❌ Docker is not running. Please start Docker Desktop first."
            exit 1
        fi
        
        docker-compose up -d
        
        echo "⏳ Waiting for services to initialize..."
        sleep 3
        
        # Check if services are healthy
        echo "🔍 Checking service health..."
        
        # Wait for PostgreSQL to be ready
        echo "   📊 PostgreSQL..."
        timeout=30
        while ! docker-compose exec -T postgres pg_isready -U oems_user > /dev/null 2>&1 && [ $timeout -gt 0 ]; do
            sleep 1
            timeout=$((timeout-1))
        done
        
        if [ $timeout -eq 0 ]; then
            echo "   ❌ PostgreSQL failed to start"
        else
            echo "   ✅ PostgreSQL ready"
        fi
        
        # Wait for backend to be ready
        echo "   🚀 Backend API..."
        timeout=60
        while ! curl -s http://localhost:8080/actuator/health > /dev/null 2>&1 && [ $timeout -gt 0 ]; do
            sleep 2
            timeout=$((timeout-2))
        done
        
        if [ $timeout -eq 0 ]; then
            echo "   ❌ Backend API not responding"
        else
            echo "   ✅ Backend API ready"
        fi
        
        echo ""
        echo "✅ OEMS Environment is running!"
        echo "================================"
        echo "🌐 Backend API:       http://localhost:8080"
        echo "📚 API Documentation: http://localhost:8080/swagger-ui.html"
        echo "🗄️  PgAdmin:          http://localhost:5050"
        echo "   └─ Email: admin@oems.com"
        echo "   └─ Password: admin123"
        echo "📊 Health Check:      http://localhost:8080/actuator/health"
        echo ""
        echo "💡 Use './scripts/oems.sh logs' to view live logs"
        echo "💡 Use './scripts/oems.sh stop' to stop all services"
        ;;
    "stop")
        echo "🛑 Stopping OEMS services..."
        docker-compose down
        
        echo ""
        echo "✅ All OEMS services have been stopped!"
        echo ""
        echo "💡 Use './scripts/oems.sh start' to restart the environment"
        echo "💡 Use './scripts/oems.sh clean' to remove all data (⚠️  destructive)"
        ;;
    "restart")
        echo "🔄 Restarting OEMS services..."
        docker-compose down
        echo "⏳ Waiting a moment before restart..."
        sleep 2
        docker-compose up -d
        
        echo "⏳ Waiting for services to initialize..."
        sleep 5
        
        echo "✅ Services restarted!"
        echo "🌐 Application: http://localhost:8080"
        echo "🗄️  PgAdmin: http://localhost:5050"
        ;;
    "logs")
        if [ -z "$2" ]; then
            echo "📋 Showing all logs..."
            docker-compose logs -f
        else
            echo "📋 Showing logs for $2..."
            docker-compose logs -f "$2"
        fi
        ;;
    "build")
        echo "🔨 Building OEMS application..."
        docker-compose build --no-cache
        echo "✅ Build completed!"
        ;;
    "clean")
        echo "🧹 Cleaning up OEMS environment..."
        echo "⚠️  This will remove all data and containers!"
        read -p "Are you sure? (y/N): " -n 1 -r
        echo
        
        if [[ $REPLY =~ ^[Yy]$ ]]; then
            echo "🗑️  Removing containers and volumes..."
            docker-compose down -v
            echo "🗑️  Cleaning Docker system..."
            docker system prune -f
            echo "✅ Complete cleanup completed!"
        else
            echo "❌ Cleanup cancelled"
        fi
        ;;
    "status")
        echo "📊 OEMS Services Status:"
        docker-compose ps
        ;;
    "db-only")
        echo "🗄️  Starting only database services..."
        docker-compose up -d postgres pgadmin
        
        echo "⏳ Waiting for database to initialize..."
        sleep 3
        
        # Check PostgreSQL health
        timeout=30
        while ! docker-compose exec -T postgres pg_isready -U oems_user > /dev/null 2>&1 && [ $timeout -gt 0 ]; do
            sleep 1
            timeout=$((timeout-1))
        done
        
        if [ $timeout -eq 0 ]; then
            echo "❌ PostgreSQL failed to start"
        else
            echo "✅ Database services ready!"
            echo "🗄️  PostgreSQL: localhost:5432 (oems_user / oems_password)"
            echo "🗄️  PgAdmin: http://localhost:5050 (admin@oems.com / admin123)"
            echo ""
            echo "💡 Use './scripts/local-dev.sh run' to start the backend locally"
        fi
        ;;
    "dev")
        echo "🚀 Starting OEMS Development Environment..."
        echo "👨‍💻 Starting database services for local development..."
        docker-compose up -d postgres pgadmin
        
        echo "⏳ Waiting for database to initialize..."
        sleep 3
        
        # Check PostgreSQL health
        timeout=30
        while ! docker-compose exec -T postgres pg_isready -U oems_user > /dev/null 2>&1 && [ $timeout -gt 0 ]; do
            sleep 1
            timeout=$((timeout-1))
        done
        
        if [ $timeout -eq 0 ]; then
            echo "❌ PostgreSQL failed to start"
            exit 1
        fi
        
        echo "✅ Development environment ready!"
        echo "================================"
        echo "🗄️  PostgreSQL: localhost:5432 (oems_user / oems_password)"
        echo "🗄️  PgAdmin: http://localhost:5050 (admin@oems.com / admin123)"
        echo ""
        echo "💡 Next steps:"
        echo "   • Run './scripts/local-dev.sh run' to start the backend"
        echo "   • Or open your IDE and run the Spring Boot application"
        ;;
    *)
        echo "Usage: ./oems.sh [command]"
        echo ""
        echo "Commands:"
        echo "  start     - Start all services (full Docker environment)"
        echo "  stop      - Stop all services"
        echo "  restart   - Restart all services"
        echo "  dev       - Start database only (for local development)"
        echo "  logs      - Show logs for all services"
        echo "  logs [service] - Show logs for specific service"
        echo "  build     - Build the application"
        echo "  clean     - Clean up containers and volumes (⚠️  destructive)"
        echo "  status    - Show service status"
        echo "  db-only   - Start only database services"
        echo ""
        echo "Examples:"
        echo "  ./scripts/oems.sh start          # Full Docker environment"
        echo "  ./scripts/oems.sh dev            # Development mode (DB only)"
        echo "  ./scripts/oems.sh logs oems-backend  # View backend logs"
        echo "  ./scripts/oems.sh stop           # Stop everything"
        ;;
esac