# Base image
FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

# Copy source code and Gradle wrapper
COPY . /app

# Grant execution permission for gradlew
RUN chmod +x ./gradlew

# Build application using bootJar
RUN ./gradlew clean bootJar --stacktrace --info

# Check if JAR file is created
RUN ls -l ./build/libs

# Copy the built JAR file
COPY ./build/libs/*.jar /app.jar

# Expose application port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "/app.jar"]
