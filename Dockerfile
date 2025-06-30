# Use OpenJDK 17
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy JAR file into container
COPY target/*.jar app.jar

# Command to run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]