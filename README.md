# OEMS - Online Event Management System

A modern, full-stack event management platform built with React (Vite) frontend and Spring Boot backend, designed to streamline event organization and management.

## 🚧 Development Status

**🔄 Project is currently in active development**

This project is being actively developed and new features are being added regularly. Some functionality may be incomplete or subject to change.

## 🏗️ Architecture Overview

OEMS follows a modern full-stack architecture with clear separation between frontend and backend:

```
┌─────────────────────┐    ┌─────────────────────┐
│    React Frontend   │    │  Spring Boot API    │
│  (Vite + Tailwind)  │◄──►│     Backend         │
│    (Port 5173)      │    │    (Port 8080)      │
└─────────────────────┘    └─────────────────────┘
           │                          │
           │                          │
           ▼                          ▼
    Modern UI/UX with           RESTful APIs &
    Responsive Design          Business Logic
```

## 🚀 Features (Planned/In Development)

- **Event Creation & Management**: Create, edit, and manage events
- **User Registration & Authentication**: Secure user accounts and sessions
- **Event Registration**: Allow users to register for events
- **Dashboard**: Comprehensive admin and user dashboards
- **Real-time Updates**: Live event updates and notifications
- **Responsive Design**: Mobile-first approach with Tailwind CSS
- **Modern UI**: Clean and intuitive user interface

## 🛠️ Technology Stack

### Frontend

- **React 18**: Modern React with hooks
- **Vite**: Fast build tool and development server
- **Tailwind CSS**: Utility-first CSS framework
- **JavaScript/TypeScript**: Modern ES6+ features

### Backend

- **Spring Boot**: Java-based backend framework
- **Spring Security**: Authentication and authorization
- **Spring Data JPA**: Database abstraction layer
- **H2 Database**: In-memory database for development
- **Maven**: Dependency management and build tool

## 📋 Prerequisites

- **Node.js**: 18.x or higher
- **npm/yarn**: Latest version
- **Java**: 17 or higher
- **Maven**: 3.6 or higher
- **Git**: Latest version

## 🛠️ Project Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
cd oems
```

### 2. Frontend Setup

```bash
cd frontend
npm install
# or
yarn install
```

### 3. Backend Setup

```bash
cd backend
mvn clean compile
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

- **Frontend**: http://localhost:5173
- **Backend API**: http://localhost:8080/api
- **H2 Database Console**: http://localhost:8080/h2-console

## 📡 API Endpoints (Planned)

### Authentication

```http
POST /api/auth/register
POST /api/auth/login
POST /api/auth/logout
GET /api/auth/profile
```

### Events

```http
GET /api/events
POST /api/events
GET /api/events/{id}
PUT /api/events/{id}
DELETE /api/events/{id}
```

### Event Registration

```http
POST /api/events/{id}/register
DELETE /api/events/{id}/unregister
GET /api/events/{id}/attendees
```

### User Dashboard

```http
GET /api/users/dashboard
GET /api/users/registered-events
GET /api/users/created-events
```

## 📁 Project Structure

```
oems/
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
├── docs/                         # Documentation
├── .gitignore
└── README.md                     # This file
```

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

## 🚨 Troubleshooting

### Common Issues

#### 1. Port Already in Use

```bash
# Frontend (Port 5173)
lsof -ti:5173 | xargs kill -9

# Backend (Port 8080)
lsof -ti:8080 | xargs kill -9
```

#### 2. npm/yarn Install Issues

```bash
# Clear cache
npm cache clean --force
# or
yarn cache clean

# Delete node_modules and reinstall
rm -rf node_modules package-lock.json
npm install
```

#### 3. Maven Build Issues

```bash
# Clean and compile
mvn clean compile

# Skip tests if needed
mvn clean compile -DskipTests
```

#### 4. CORS Issues

Ensure backend CORS configuration allows frontend origin:

```java
@CrossOrigin(origins = "http://localhost:5173")
```

## 🎨 UI/UX Guidelines

### Design System

- **Primary Colors**: Define your brand colors
- **Typography**: Consistent font hierarchy
- **Spacing**: Tailwind spacing scale
- **Components**: Reusable UI components

### Responsive Breakpoints

```css
/* Mobile first approach */
sm: 640px   /* Small devices */
md: 768px   /* Medium devices */
lg: 1024px  /* Large devices */
xl: 1280px  /* Extra large devices */
```

## 🔒 Security Considerations

- **Authentication**: JWT-based authentication
- **Authorization**: Role-based access control
- **Input Validation**: Server-side validation for all inputs
- **CORS**: Proper CORS configuration
- **Environment Variables**: Secure storage of sensitive data

## 🚀 Deployment (Future)

### Frontend Deployment Options

- **Netlify**: Easy static site deployment
- **Vercel**: Optimized for React applications
- **AWS S3 + CloudFront**: Scalable static hosting

### Backend Deployment Options

- **Heroku**: Simple Java application deployment
- **AWS EC2**: Full control over server environment
- **Docker**: Containerized deployment

## 🤝 Contributors

- **[SoulfulSpy](https://github.com/SoulfulSpy)** - Full Stack Developer
- **[BiswayanPaul](https://github.com/BiswayanPaul)** - Full Stack Developer

## 🛣️ Roadmap

### Phase 1 (Current)

- [x] Project setup and architecture
- [ ] User authentication system
- [ ] Basic event CRUD operations
- [ ] Frontend UI components

### Phase 2 (Upcoming)

- [ ] Event registration system
- [ ] User dashboard
- [ ] Admin panel
- [ ] Email notifications

### Phase 3 (Future)

- [ ] Advanced search and filtering
- [ ] Event categories and tags
- [ ] Payment integration
- [ ] Mobile app development

## 📝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Development Guidelines

- Follow React best practices and hooks patterns
- Use Tailwind CSS for styling consistently
- Write clean, commented code
- Follow Spring Boot conventions
- Write tests for new features

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Support

For questions, issues, or contributions:

- Open an issue in the GitHub repository
- Contact the contributors directly
- Check the documentation in the `/docs` folder

---

**🚧 Remember: This project is in active development. Features and documentation are subject to change.**

**Happy Coding! 🎉**
