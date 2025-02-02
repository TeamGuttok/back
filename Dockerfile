# Base image
FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

# Copy source code
COPY . /app

# Grant execution permission for Gradle
RUN chmod +x ./gradlew

# Build application using bootJar
RUN ./gradlew clean bootJar -x test --stacktrace --info || (echo "Gradle bootJar build failed!" && exit 1)

# 확인용 - 빌드된 JAR 파일 목록 출력
RUN ls -lh build/libs/

# Copy the bootJar JAR file
COPY build/libs/*.jar /app/app.jar

# Expose application port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "/app/app.jar"]
