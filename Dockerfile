# Base image
FROM openjdk:21-jdk-slim

# Grant execution permission for Gradle
RUN chmod +x ./gradlew

# Copy the bootJar JAR file dynamically
RUN JAR_FILE=$(ls build/libs/guttok-back-*.jar | tail -n 1) && cp "$JAR_FILE"

# 확인용 - 빌드된 JAR 파일 목록 출력
RUN ls -lh build/libs/

# Copy the specific bootJar JAR file
RUN cp build/libs/guttok-back-0.0.1-SNAPSHOT.jar

# Expose application port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "/app.jar"]
