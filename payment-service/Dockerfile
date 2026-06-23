# BƯỚC 1: Tải và Cache các thư viện (Dependencies)
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Chỉ copy duy nhất file pom.xml vào trước
COPY pom.xml .

# Chạy lệnh này để Maven tải trước TẤT CẢ thư viện về và đóng băng layer này lại
RUN mvn dependency:go-offline -B

# BƯỚC 2: Copy code và Build (Lần sau sửa code chỉ chạy từ bước này, không tải lại lib nữa)
COPY src ./src
RUN mvn clean package -DskipTests -B

# BƯỚC 3: Đóng gói JRE để chạy ứng dụng
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]