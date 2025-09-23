# Use an official OpenJDK 21 image as the base image
FROM eclipse-temurin:21-jdk-alpine AS build

# Set working directory inside the container
WORKDIR /app

# Copy Gradle wrapper and gradle files
COPY gradlew .
COPY gradle ./gradle

# Copy the backend source code
COPY src ./src
COPY build.gradle .

# Give execute permissions for gradlew
RUN chmod +x ./gradlew

# Build the Spring Boot fat jar (replace `bootJar` if you use a different task)
RUN ./gradlew bootJar --no-daemon

# Second stage: run the jar on a lightweight JRE image
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose port (default Spring Boot port)
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
