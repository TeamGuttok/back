# Base image
FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

# Copy source code and Gradle files
COPY . /app

# Install dependencies and build the application
RUN ./gradlew clean bootJar

# Copy the built application JAR
ARG JAR_FILE=./build/libs/guttok-back-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} /app.jar

# Expose application port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "/app.jar"]
