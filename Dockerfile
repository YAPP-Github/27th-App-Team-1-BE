FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

# Gradle wrapper 권한 설정
COPY gradle/ gradle/
COPY gradlew ./
COPY gradlew.bat ./
RUN chmod +x ./gradlew

# 빌드 설정 파일 복사 (의존성 캐시 최적화)
COPY build.gradle settings.gradle gradle.properties ./
COPY application/build.gradle application/
COPY domain/build.gradle domain/
COPY domain/domain-service/build.gradle domain/domain-service/
COPY domain/domain-rdb/build.gradle domain/domain-rdb/
COPY domain/domain-redis/build.gradle domain/domain-redis/
COPY clients/build.gradle clients/
COPY support/build.gradle support/
COPY common/build.gradle common/

# 의존성 다운로드 (소스 코드 변경 없이 캐시 활용)
RUN ./gradlew dependencies --no-daemon || true

# 소스 코드 복사 및 빌드
COPY . .
RUN ./gradlew :application:bootJar -x test --no-daemon

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/application/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
