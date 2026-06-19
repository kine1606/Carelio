FROM maven:3.9.6-eclipse-temurin-17 AS dev
WORKDIR /app

# Copy file cấu hình trước để tận dụng cache của Docker
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy toàn bộ mã nguồn vào container
COPY src ./src

# Chạy ứng dụng bằng lệnh của Spring Boot, bật tính năng lắng nghe DevTools
CMD ["mvn", "spring-boot:run", "-Dspring-boot.run.profiles=dev"]