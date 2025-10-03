#!/bin/bash

# ════════════════════════════════════════════════════════════════
# 🚀 OEMS BACKEND - QUICK START REFERENCE
# ════════════════════════════════════════════════════════════════

cat << 'EOF'

╔═══════════════════════════════════════════════════════════════╗
║                  🚀 OEMS BACKEND QUICK START                  ║
╚═══════════════════════════════════════════════════════════════╝

📋 SYSTEM STATUS
────────────────
✅ All code compiled successfully (50 files)
✅ Zero compilation errors
✅ 60+ new API endpoints ready
✅ Multi-admin system fully implemented

═══════════════════════════════════════════════════════════════

🎯 START THE BACKEND (Choose ONE option)

┌───────────────────────────────────────────────────────────────┐
│ OPTION 1: Full Docker Environment (Recommended)              │
│                                                               │
│   ./scripts/oems.sh start                                     │
│                                                               │
│   ✅ Starts: Database + Backend + PgAdmin                     │
│   🌐 API: http://localhost:8080                               │
│   📚 Docs: http://localhost:8080/swagger-ui.html              │
│   🗄️  PgAdmin: http://localhost:5050                          │
└───────────────────────────────────────────────────────────────┘

┌───────────────────────────────────────────────────────────────┐
│ OPTION 2: Development Mode (For Coding)                      │
│                                                               │
│   # Terminal 1: Start database                               │
│   ./scripts/oems.sh dev                                       │
│                                                               │
│   # Terminal 2: Run backend with hot-reload                  │
│   ./scripts/local-dev.sh dev                                  │
│                                                               │
│   ✅ Starts: Database only in Docker                          │
│   🔥 Backend: Runs locally with auto-restart                  │
│   💻 IDE: Can debug and breakpoint                            │
└───────────────────────────────────────────────────────────────┘

┌───────────────────────────────────────────────────────────────┐
│ OPTION 3: Database Only (For IDE Development)                │
│                                                               │
│   ./scripts/oems.sh db-only                                   │
│                                                               │
│   Then run BackendApplication.java from your IDE             │
│                                                               │
│   ✅ Starts: PostgreSQL + PgAdmin only                        │
│   💻 IDE: Run main class directly                             │
└───────────────────────────────────────────────────────────────┘

═══════════════════════════════════════════════════════════════

📝 COMMON COMMANDS

Stop Services:
  ./scripts/oems.sh stop

Restart Services:
  ./scripts/oems.sh restart

View Logs:
  ./scripts/oems.sh logs
  ./scripts/oems.sh logs oems-backend

Check Status:
  ./scripts/oems.sh status

Run Tests:
  ./scripts/local-dev.sh test

Build Package:
  ./scripts/local-dev.sh package

Clean Build:
  ./scripts/local-dev.sh clean

═══════════════════════════════════════════════════════════════

🗄️  DATABASE ACCESS

PostgreSQL:
  Host: localhost
  Port: 5432
  Database: oems_db
  Username: oems_user
  Password: oems_password

PgAdmin (http://localhost:5050):
  Email: admin@oems.com
  Password: admin123

Connection String:
  jdbc:postgresql://localhost:5432/oems_db

═══════════════════════════════════════════════════════════════

🌐 API ENDPOINTS

Base URL: http://localhost:8080

Health Check:
  GET /actuator/health

API Documentation:
  GET /swagger-ui.html

Customer APIs:
  /api/auth/*
  /api/rides/*
  /api/payments/*

Driver APIs:
  /api/driver/*

Showroom Admin APIs:
  /api/showroom-admin/*

Super Admin APIs:
  /api/super-admin/*

Driver Admin APIs:
  /api/driver-admin/*

═══════════════════════════════════════════════════════════════

📊 NEW FEATURES IMPLEMENTED

✅ Showroom Admin System
   - Driver management
   - Vehicle fleet control
   - Ride monitoring
   - Analytics dashboard

✅ Super Admin System (Naiyo24)
   - Platform-wide control
   - User management
   - Showroom management
   - Financial reports
   - System configuration

✅ Driver App Backend
   - Ride acceptance/rejection
   - Location tracking
   - Trip management
   - Earnings analytics
   - Performance metrics

✅ Driver Admin System
   - Advanced analytics
   - Support ticketing
   - Fleet management
   - Market insights

═══════════════════════════════════════════════════════════════

⚙️  BEFORE FIRST RUN

1. Run Database Migration:
   psql -U oems_user -d oems_db -f database/migrations/001_create_admin_system.sql

   OR connect via PgAdmin and execute the SQL file

2. Make scripts executable (if not already):
   chmod +x scripts/oems.sh scripts/local-dev.sh

═══════════════════════════════════════════════════════════════

📖 DOCUMENTATION

Implementation Guide:
  ADMIN_SYSTEM_IMPLEMENTATION.md

Status Report:
  STATUS_REPORT.md

Main README:
  README.md

API Documentation (when running):
  http://localhost:8080/swagger-ui.html

═══════════════════════════════════════════════════════════════

💡 TROUBLESHOOTING

Backend won't start?
  → Check if port 8080 is free: lsof -i :8080
  → Check database is running: ./scripts/oems.sh status
  → View logs: ./scripts/oems.sh logs oems-backend

Database connection error?
  → Start database: ./scripts/oems.sh dev
  → Check credentials in application.properties
  → Test connection: pg_isready -h localhost -p 5432

Port already in use?
  → Stop other services: ./scripts/oems.sh stop
  → Kill process: kill $(lsof -ti:8080)

Docker issues?
  → Restart Docker Desktop
  → Check Docker: docker info
  → Clean restart: ./scripts/oems.sh restart

═══════════════════════════════════════════════════════════════

🎯 QUICK START (FIRST TIME)

cd /Users/biswayanpaul/Developer/Naiyo24/OEMS/backend

# Make scripts executable
chmod +x scripts/oems.sh scripts/local-dev.sh

# Start everything
./scripts/oems.sh start

# Wait for startup (30-60 seconds)

# Test the API
curl http://localhost:8080/actuator/health

# Open API docs
open http://localhost:8080/swagger-ui.html

# View logs
./scripts/oems.sh logs

╔═══════════════════════════════════════════════════════════════╗
║              🎉 Your backend is ready to go! 🚀               ║
╚═══════════════════════════════════════════════════════════════╝

EOF
