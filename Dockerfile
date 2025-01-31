# 1️⃣ Build Stage
FROM openjdk:21-jdk-slim AS build

WORKDIR /app

# Gradle wrapper 및 소스 코드 복사
COPY . /app

# 실행 권한 부여 및 빌드 실행
RUN chmod +x ./gradlew
RUN ./gradlew clean bootJar --stacktrace --info

# Runtime Stage
FROM openjdk:21-jdk-slim

WORKDIR /app

# 빌드된 JAR 파일 복사
COPY --from=build /app/build/libs/*.jar /app.jar

# 포트 설정
EXPOSE 8080

# 애플리케이션 실행
CMD ["java", "-jar", "/app.jar"]
