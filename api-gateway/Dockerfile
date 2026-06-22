# Bước 1: Dùng Maven để build ra file JAR (tối ưu layer)
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY pom.xml .
COPY src ./src
RUN mvn -B -e -DskipTests package

# Bước 2: Chạy file JAR đó bằng Java (nhẹ hơn với JRE)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Kích hoạt cấu hình dành cho môi trường Docker
ENV SPRING_PROFILES_ACTIVE=docker
# Thiết lập JVM thân thiện với container
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75 -XX:InitialRAMPercentage=25 -Djava.security.egd=file:/dev/./urandom"
COPY --from=build /workspace/target/*.jar app.jar
EXPOSE 8000
ENTRYPOINT ["java", "-jar", "app.jar"]