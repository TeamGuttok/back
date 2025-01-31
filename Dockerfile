# Base image
FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

# Copy source code and Gradle wrapper
COPY . /app

# Grant execution permission for Gradlew
RUN chmod +x ./gradlew

# Build application using bootJar
RUN ./gradlew clean bootJar --stacktrace --info

# Build arguments
ARG JAR_FILE=build/libs/*.jar

# Copy the application JAR
COPY ${JAR_FILE} app.jar

# Expose application port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "/app.jar"]
