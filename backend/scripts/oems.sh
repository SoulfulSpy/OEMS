#!/bin/bash

# OEMS Docker Environment Management Script

echo "🚀 OEMS Docker Environment Manager"
echo "=================================="

case "$1" in
    "start")
        echo "Starting OEMS services..."
        docker-compose up -d
        echo "✅ Services started!"
        echo "🌐 Application: http://localhost:8080"
        echo "🗄️  PgAdmin: http://localhost:5050 (admin@oems.com / admin123)"
        echo "📊 Health Check: http://localhost:8080/actuator/health"
        ;;
    "stop")
        echo "Stopping OEMS services..."
        docker-compose down
        echo "✅ Services stopped!"
        ;;
    "restart")
        echo "Restarting OEMS services..."
        docker-compose down
        docker-compose up -d
        echo "✅ Services restarted!"
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
        docker-compose down -v
        docker system prune -f
        echo "✅ Cleanup completed!"
        ;;
    "status")
        echo "📊 OEMS Services Status:"
        docker-compose ps
        ;;
    "db-only")
        echo "🗄️  Starting only database services..."
        docker-compose up -d postgres pgadmin
        echo "✅ Database services started!"
        echo "🗄️  PgAdmin: http://localhost:5050 (admin@oems.com / admin123)"
        ;;
    *)
        echo "Usage: ./oems.sh [command]"
        echo ""
        echo "Commands:"
        echo "  start     - Start all services"
        echo "  stop      - Stop all services"
        echo "  restart   - Restart all services"
        echo "  logs      - Show logs for all services"
        echo "  logs [service] - Show logs for specific service"
        echo "  build     - Build the application"
        echo "  clean     - Clean up containers and volumes"
        echo "  status    - Show service status"
        echo "  db-only   - Start only database services"
        echo ""
        echo "Examples:"
        echo "  ./oems.sh start"
        echo "  ./oems.sh logs oems-backend"
        echo "  ./oems.sh db-only"
        ;;
esac