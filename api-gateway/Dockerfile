# Bước 1: Build
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
# Thêm clean để chắc chắn không dính rác
RUN mvn clean package -DskipTests

# Bước 2: Chạy
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
# Thêm dấu điền chính xác nếu bạn biết tên, hoặc dùng cách dưới để tránh lấy nhầm file 'plain.jar'
COPY --from=build /app/target/api-gateway-*.jar app.jar
EXPOSE 8000
ENTRYPOINT ["java", "-jar", "app.jar"]