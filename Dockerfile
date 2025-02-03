# Base Image
FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

# Install MySQL and Redis
RUN apt-get update && apt-get install -y \
    mysql-server \
    redis-server \
    && rm -rf /var/lib/apt/lists/*

# Copy source code
COPY . /app

# Grant execution permission for Gradle
RUN chmod +x ./gradlew

# Remove existing JAR files and build application using bootJar
RUN rm -rf build/libs/*.jar && ./gradlew clean bootJar -x test --stacktrace --info || (echo "Gradle bootJar build failed!" && exit 1)

# 확인용 - 빌드된 JAR 파일 목록 출력
RUN ls -lh build/libs/

# Find the generated bootJar file dynamically and copy it to /app.jar
RUN JAR_FILE=$(ls build/libs/*-back-*.jar | tail -n 1) && cp "$JAR_FILE" /app.jar

# Copy entrypoint script
COPY entrypoint.sh /app/entrypoint.sh
RUN chmod +x /app/entrypoint.sh

# Expose application port
EXPOSE 8080

# Set entrypoint to start MySQL, Redis, and Spring Boot application
ENTRYPOINT ["/app/entrypoint.sh"]
