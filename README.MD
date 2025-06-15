# Project Base

This is a Java Spring Boot project base designed to provide a solid foundation for building scalable and maintainable web applications. It includes common configurations, utilities, and best practices for rapid development. The project is structured to help developers quickly start new projects with a robust architecture, security, and essential features out of the box.

## Features
- **Spring Boot application structure**: Modular and layered architecture for easy maintenance and scalability.
- **RESTful API base with versioning**: Easily extendable API endpoints with version control.
- **Exception handling and global error responses**: Centralized error management for consistent API responses.
- **Security configuration with JWT authentication**: Secure endpoints using JSON Web Tokens and role-based access control.
- **User and role management**: Built-in user authentication, registration, and role assignment.
- **File upload utilities**: Support for uploading and validating files and images.
- **Email sending utilities**: Easily send emails using configurable mail services.
- **Internationalization (i18n) support**: Multi-language support for messages and validation.
- **Aspect-Oriented Programming (AOP) for repository layer**: Cross-cutting concerns like logging and auditing handled via aspects.
- **Configurable properties for cloud services and mail**: Centralized configuration for cloud storage and email providers.
- **Pagination utilities**: Helpers for paginated API responses.

## Project Structure
```
project-base/
├── mvnw, mvnw.cmd           # Maven wrapper scripts
├── pom.xml                  # Maven project configuration
├── src/
│   ├── main/
│   │   ├── java/com/example/projectbase/
│   │   │   ├── aop/         # Aspects and annotations
│   │   │   ├── base/        # Base REST API and response classes
│   │   │   ├── config/      # Application configuration
│   │   │   ├── constant/    # Constants used throughout the project
│   │   │   ├── controller/  # REST controllers
│   │   │   ├── domain/      # DTOs, entities, mappers
│   │   │   ├── exception/   # Custom exceptions and handlers
│   │   │   ├── job/         # Scheduled jobs (if any)
│   │   │   ├── repository/  # Spring Data repositories
│   │   │   ├── security/    # Security and JWT configuration
│   │   │   ├── service/     # Service interfaces and implementations
│   │   │   ├── util/        # Utility classes
│   │   │   └── validator/   # Custom validators and annotations
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── i18n/        # Internationalization files
│   │       ├── static/      # Static resources
│   │       └── templates/   # Thymeleaf templates
│   └── test/
│       └── java/com/example/projectbase/ # Unit and integration tests
```

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Build and Run
```bash
./mvnw clean install
./mvnw spring-boot:run
```

The application will start on [http://localhost:8080](http://localhost:8080) by default.

### Configuration
Edit `src/main/resources/application.properties` to configure database, mail, cloud services, and other environment-specific settings. Internationalization files are located in `src/main/resources/i18n/`.

## Testing
Unit and integration tests are located under `src/test/java/com/example/projectbase/`. Run tests with:
```bash
./mvnw test
```

## Contributing
Feel free to fork this repository and submit pull requests. Contributions are welcome! Please follow the existing code style and add tests for new features.

## License
This project is provided as a base and does not include a license by default. Add your preferred license as needed.