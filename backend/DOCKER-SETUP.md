# OEMS Backend - Docker Setup

This guide will help you run the OEMS (Online Employee Management System) backend using Docker.

## Prerequisites

- Docker Desktop installed and running
- Docker Compose v3.8+
- Git (for cloning the repository)

## Quick Start

### 1. Clone and Navigate

```bash
git clone <your-repo-url>
cd OEMS/backend
```

### 2. Environment Configuration

Copy and customize the environment file:

```bash
cp .env .env.local
# Edit .env.local with your specific configuration
```

### 3. Start All Services

```bash
# Using Docker Compose directly
docker-compose up -d

# Or using the management script
./scripts/oems.sh start
```

### 4. Access Your Application

- **Spring Boot API**: http://localhost:8080
- **PgAdmin** (for team members): http://localhost:5050
- **Health Check**: http://localhost:8080/actuator/health
- **API Documentation**: http://localhost:8080/swagger-ui.html

## Development Workflows

### Option 1: Full Docker Environment

Best for production-like environment and team collaboration.

```bash
# Start all services (PostgreSQL + PgAdmin + Spring Boot)
./scripts/oems.sh start

# View logs
./scripts/oems.sh logs

# Stop all services
./scripts/oems.sh stop
```

### Option 2: Local Development with Docker Database

Best for active development - faster restart times.

```bash
# Start only database services
./scripts/oems.sh db-only

# Run Spring Boot locally
./scripts/local-dev.sh run
# Or directly with Maven
mvn spring-boot:run
```

## Service Details

### PostgreSQL Database

- **Port**: 5432
- **Database**: oems_db
- **Username**: oems_user
- **Password**: oems_password
- **Container**: oems-postgres

### PgAdmin (Database Management)

- **Port**: 5050
- **Email**: admin@oems.com
- **Password**: admin123
- **Container**: oems-pgadmin

### Spring Boot Application

- **Port**: 8080
- **Container**: oems-backend
- **Health Check**: http://localhost:8080/actuator/health

## Useful Commands

### Docker Management

```bash
# Start services
./scripts/oems.sh start

# Stop services
./scripts/oems.sh stop

# Restart services
./scripts/oems.sh restart

# View all logs
./scripts/oems.sh logs

# View specific service logs
./scripts/oems.sh logs oems-backend

# Check service status
./scripts/oems.sh status

# Rebuild application
./scripts/oems.sh build

# Clean everything (removes volumes)
./scripts/oems.sh clean
```

### Local Development

```bash
# Run application locally (with Docker database)
./scripts/local-dev.sh run

# Run tests
./scripts/local-dev.sh test

# Clean and compile
./scripts/local-dev.sh clean

# Build package
./scripts/local-dev.sh package
```

## Database Connection Details

### For Local Development (Docker Database)

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/oems_db
spring.datasource.username=oems_user
spring.datasource.password=oems_password
```

### For PgAdmin Connection

When accessing through PgAdmin:

- **Host**: postgres (Docker internal network)
- **Port**: 5432
- **Database**: oems_db
- **Username**: oems_user
- **Password**: oems_password

## Environment Variables

Key environment variables you can customize in `.env`:

```bash
# Database
POSTGRES_DB=oems_db
POSTGRES_USER=oems_user
POSTGRES_PASSWORD=oems_password

# Application
JPA_DDL_AUTO=update
JPA_SHOW_SQL=true

# Security
JWT_SECRET=your-secret-key-change-this-in-production

# Twilio (for SMS)
TWILIO_ACCOUNT_SID=your_account_sid
TWILIO_AUTH_TOKEN=your_auth_token
TWILIO_FROM_NUMBER=your_phone_number
```

## Troubleshooting

### Application Won't Start

```bash
# Check if ports are available
lsof -i :8080
lsof -i :5432
lsof -i :5050

# Check Docker logs
./scripts/oems.sh logs oems-backend

# Restart with fresh build
./scripts/oems.sh clean
./scripts/oems.sh build
./scripts/oems.sh start
```

### Database Connection Issues

```bash
# Check if PostgreSQL is running
docker-compose ps postgres

# Check database logs
./scripts/oems.sh logs postgres

# Reset database (WARNING: Deletes all data)
./scripts/oems.sh clean
./scripts/oems.sh start
```

### Cannot Access PgAdmin

1. Wait for services to fully start (can take 1-2 minutes)
2. Check if container is running: `docker-compose ps pgladmin`
3. Access at http://localhost:5050
4. Use credentials: admin@oems.com / admin123

## Production Considerations

1. **Change Default Passwords**: Update all default credentials in `.env`
2. **JWT Secret**: Use a secure, random JWT secret key
3. **Database Backup**: Set up regular PostgreSQL backups
4. **SSL/TLS**: Configure HTTPS for production
5. **Resource Limits**: Set appropriate memory/CPU limits in docker-compose.yml

## API Testing

Once the application is running, you can test the API endpoints:

```bash
# Health check
curl http://localhost:8080/actuator/health

# API documentation
open http://localhost:8080/swagger-ui.html
```

## Team Collaboration

- **Developers**: Use local development mode (`./scripts/local-dev.sh run`)
- **Testers/Others**: Use full Docker mode (`./scripts/oems.sh start`)
- **Database Access**: Everyone can use PgAdmin at http://localhost:5050

## Support

For issues and questions:

1. Check the logs: `./scripts/oems.sh logs`
2. Verify service status: `./scripts/oems.sh status`
3. Try clean restart: `./scripts/oems.sh clean && ./scripts/oems.sh start`
