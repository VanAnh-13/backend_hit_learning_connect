# HIT Learning Connect (Backend)

An online learning platform backend built with **Spring Boot**, **Redis**, **MySQL**, and **Docker**. This system provides RESTful APIs for managing courses, users, enrollments, and other learning platform features.

## Project Overview

HIT Learning Connect is the backend server for an online learning platform. It provides core functionalities to manage users and courses, handle enrollments, and enforce authentication/authorization. The backend is implemented in Java using Spring Boot, with a layered architecture (controller, service, repository) to separate concerns. It leverages a MySQL database for persistent data storage and uses Redis for caching and fast access to frequently used data. Docker support is included to simplify deployment, allowing the entire system (application, MySQL, Redis) to run in containerized environments.

## Features

- **User Management**: Registration, login, profile updates, and role-based access (Student, Instructor, Admin) secured with Spring Security and JWT.
- **Course Management**: Create, update, delete, and list courses with details like title, description, instructor, and schedule.
- **Enrollment**: Students can enroll in courses; instructors/admins can approve or reject requests, view enrolled students, or cancel registrations.
- **Authentication & Authorization**: JWT-based stateless authentication, password hashing, and role-based endpoint protection.  
- **Real-time Notifications**: Prepared for WebSocket-based notifications (e.g., live announcements, chat).
- **File Uploads**: Integration with Cloudinary for media and document uploads.  
- **Email Service**: Configurable SMTP/SendGrid for account verification, password resets, and announcements.  
- **Caching**: Redis caching for frequently accessed data and potential session/token management.  
- **API Documentation**: Auto-generated Swagger UI via Springdoc OpenAPI at `/swagger-ui.html`.

## Tech Stack

- **Java 17**, **Spring Boot 3.x** (Spring MVC, Data JPA, Security, WebSocket)  
- **MySQL** with Spring Data JPA (Hibernate)  
- **Redis** for caching  
- **JWT** for authentication  
- **Docker** & **Docker Compose** for containerization  
- **Maven** for build & dependency management  
- **Lombok**, **MapStruct**, **Cloudinary API**, **SendGrid API**, **Springdoc OpenAPI**

## Getting Started

### Prerequisites

- JDK 17+  
- Maven  
- MySQL (or via Docker)  
- Redis (or via Docker)  
- Docker & Docker Compose (optional)

### Local Setup

1. **Clone repo**  
   ```bash
   git clone https://github.com/VanAnh-13/backend_hit_learning_connect.git
   cd backend_hit_learning_connect
2. **Configure (DB, Redis, JWT_SECRET, Cloudinary, email).**
   ```bash
   src/main/resources/application.properties
3. **Build**
   ```bash
   mvn clean install
4. **Run**
   ```bash
   mvn spring-boot:run
    # or
    java -jar target/Hit-LC-0.0.1-SNAPSHOT.jar
5. **Access Swagger**
   ```bash
   http://localhost:8080/swagger-ui.html
### Docker Setup

**Docker Compose (if available):**
```bash
docker-compose up --build
```
### Porject Structure
```bash
backend_hit_learning_connect/
├── src/
│   ├── main/java/com/example/
│   │   ├── controller/      # REST Controllers
│   │   ├── service/         # Business logic
│   │   ├── repository/      # JPA Repositories
│   │   ├── domain/
│   │   │   ├── entity/      # JPA Entities
│   │   │   └── model/       # DTOs, enums
│   │   ├── security/        # JWT & Spring Security
│   │   └── config/          # App configurations
│   └── resources/
│       ├── application.properties
│       └── i18n/            # (Optional) Localization files
├── Dockerfile
├── docker-compose.yml
└── pom.xml
```
### Contributing
Contributions are welcome! Please fork the repo, create a feature branch, and open a pull request. For major changes, open an issue first to discuss your ideas. Follow existing code style, add tests, and update documentation as needed.
