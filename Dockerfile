# Base image
FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

# Copy source code and Gradle wrapper
COPY . /app

# Grant execution permission for Gradlew
RUN chmod +x ./gradlew

# Build application using bootJar
RUN ./gradlew clean bootJar --stacktrace --info || (echo "Gradle build failed!" && exit 1)

# List the build/libs directory to check if JAR was created
RUN ls -lh build/libs/

# Copy the application JAR
COPY build/libs/*.jar /app/app.jar

# Expose application port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "/app/app.jar"]
