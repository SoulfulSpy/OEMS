# OEMS - On-demand Electronic Mobility Service

A modern, scalable ride-hailing platform connecting riders with drivers through intuitive mobile and web applications. Built with cutting-edge technology to deliver seamless transportation experiences.

## 🚧 Development Status

**🔄 Project is currently in active development**

This project is being actively developed with new features and improvements being added regularly. The platform is designed for scalability, reliability, and real-time performance.

## 🏗️ System Overview

OEMS is built on a modern full-stack architecture designed for scalability and maintainability:

```
┌─────────────────────┐    ┌─────────────────────┐
│   React Frontend    │    │  Spring Boot API    │
│  (Admin Portal)     │◄──►│     Backend         │
│    (Port 3000)      │    │    (Port 8080)      │
└─────────────────────┘    └─────────────────────┘
           │                          │
           │                          │
           ▼                          ▼
    Web-based Admin            RESTful APIs &
    Dashboard & Portal         Microservices Logic
```

## 🚀 Key Features (In Development)

### Core Platform Features

- **User Management**: Comprehensive user registration and authentication
- **Admin Dashboard**: Web-based administrative interface
- **Real-time Operations**: Live data management and monitoring
- **Secure Architecture**: JWT-based authentication and authorization
- **Scalable Backend**: Microservices-ready Spring Boot architecture

### Planned Features

- **Trip Management**: Complete ride booking and management system
- **Driver & Customer Portals**: Dedicated interfaces for different user types
- **Payment Integration**: Secure payment processing
- **Location Services**: GPS tracking and geospatial operations
- **Analytics & Reporting**: Business intelligence and performance metrics

## 🛠️ Technology Stack

### Frontend

- **React 18**: Modern frontend framework with hooks
- **TypeScript**: Type-safe JavaScript development
- **Tailwind CSS**: Utility-first CSS framework
- **Vite**: Fast build tool and development server

### Backend

- **Java 21+**: Modern Java with Spring Boot framework
- **Spring Boot**: Comprehensive backend framework
- **PostgreSQL**: Primary database for data persistence
- **Maven**: Dependency management and build tool

### Development Tools

- **Git**: Version control
- **REST APIs**: Service communication
- **JWT**: Authentication and authorization

## 📋 Prerequisites

- **Node.js**: 18.x or higher
- **npm/yarn**: Latest version
- **Java**: 21 or higher
- **Maven**: 3.6 or higher
- **PostgreSQL**: 16 or higher (optional for development)
- **Git**: Latest version

## 🛠️ Development Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
cd oems
```

### 2. Backend Setup

```bash
cd backend
mvn clean compile
```

### 3. Frontend Setup

```bash
cd frontend
npm install
# or
yarn install
```

### 4. Environment Configuration

#### Frontend Environment

Create a `.env` file in the frontend directory:

```env
VITE_API_BASE_URL=http://localhost:8080/api
VITE_APP_NAME=OEMS
```

#### Backend Configuration

Update `backend/src/main/resources/application.properties`:

```properties
server.port=8080
spring.datasource.url=jdbc:h2:mem:oemsdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true
```

## 🚀 Running the Application

### Development Mode

You need to run both frontend and backend. Open **2 separate terminal windows**:

#### Terminal 1 - Backend (Spring Boot)

```bash
cd backend
mvn spring-boot:run
```

#### Terminal 2 - Frontend (React + Vite)

```bash
cd frontend
npm run dev
# or
yarn dev
```

### Production Build

#### Build Frontend

```bash
cd frontend
npm run build
# or
yarn build
```

#### Build Backend

```bash
cd backend
mvn clean package
java -jar target/oems-backend-1.0.0.jar
```

## 🌐 Access Points

- **Frontend Application**: http://localhost:5173
- **Backend API**: http://localhost:8080/api
- **H2 Database Console**: http://localhost:8080/h2-console (if using H2)

## 📁 Project Structure

```
oems/
├── backend/                      # Spring Boot backend
│   ├── src/main/java/
│   │   └── com/oems/
│   │       ├── controller/       # REST controllers
│   │       ├── service/          # Business logic
│   │       ├── entity/           # JPA entities
│   │       ├── repository/       # Data access layer
│   │       ├── config/           # Configuration classes
│   │       ├── dto/              # Data transfer objects
│   │       └── OemsApplication.java
│   ├── src/main/resources/
│   │   ├── application.properties
│   │   └── data.sql              # Sample data
│   └── pom.xml
├── frontend/                     # React frontend
│   ├── public/                   # Static assets
│   ├── src/
│   │   ├── components/           # Reusable React components
│   │   ├── pages/               # Page components
│   │   ├── hooks/               # Custom React hooks
│   │   ├── services/            # API services
│   │   ├── utils/               # Utility functions
│   │   ├── styles/              # Global styles
│   │   ├── App.jsx              # Main app component
│   │   └── main.jsx             # Entry point
│   ├── package.json
│   ├── vite.config.js
│   └── tailwind.config.js
├── docs/                         # Documentation
├── .gitignore
└── README.md                     # This file
```

## 🔒 Security & Compliance

- **Data Encryption**: All sensitive data encrypted at rest and in transit
- **Authentication**: JWT-based authentication with refresh token rotation
- **Authorization**: Role-based access control (RBAC)
- **Privacy**: GDPR and privacy regulation compliance
- **Monitoring**: Comprehensive logging and monitoring systems

## 🧪 Testing

### Frontend Testing

```bash
cd frontend
npm run test
# or
yarn test
```

### Backend Testing

```bash
cd backend
mvn test
```

## 📊 Monitoring & Analytics

- **Real-time Metrics**: Live platform performance monitoring
- **Business Analytics**: Trip, revenue, and user engagement analytics
- **Health Checks**: Automated service health monitoring
- **Error Tracking**: Comprehensive error logging and alerting

## 🤝 Contributors

- **[SoulfulSpy](https://github.com/SoulfulSpy)** - Full Stack Developer
- **[BiswayanPaul](https://github.com/BiswayanPaul)** - Full Stack Developer

## 🛣️ Development Roadmap

### Phase 1: Foundation (Current)

- [x] Project architecture and setup
- [ ] User authentication system
- [ ] Basic admin dashboard
- [ ] Core API endpoints

### Phase 2: Core Features

- [ ] User management system
- [ ] Basic trip management
- [ ] Database integration
- [ ] API security implementation

### Phase 3: Enhanced Platform

- [ ] Mobile applications (Flutter)
- [ ] Real-time location services
- [ ] Payment integration
- [ ] Advanced admin features

### Phase 4: Advanced Features

- [ ] Microservices architecture
- [ ] Docker containerization
- [ ] Advanced analytics
- [ ] Machine learning integration

## 🚨 Support & Troubleshooting

### Common Issues

- **Port Conflicts**: Ensure ports 8080 (backend) and 5173 (frontend) are available
- **Database Connection**: Verify database configuration in application.properties
- **CORS Issues**: Check CORS configuration if frontend can't connect to backend
- **Build Failures**: Ensure all dependencies are properly installed

### Getting Help

- Check the documentation in `/docs` folder
- Review application logs for detailed error information
- Contact the development team for technical support

## 📄 License

This project is proprietary software. All rights reserved.

## 🔐 Confidentiality Notice

This codebase contains proprietary business logic and algorithms. Access is restricted to authorized personnel only. Do not share, distribute, or discuss technical implementation details outside the development team.

---

**🚀 Building the Future of Urban Mobility**

**🚧 This platform is under active development. Features and APIs are subject to change.**
