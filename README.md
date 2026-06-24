# 🚀 Carelio - Smart Home Appliance Care & Service Ecosystem

Carelio là hệ thống backend **Microservices** phục vụ quản lý thiết bị gia dụng, điều phối dịch vụ sửa chữa và tích hợp thanh toán trực tuyến.

---

## 🏗️ Architecture Overview

* **API Gateway** (Port 8000): Entry point, routing, timeout, CORS
* **Keycloak** (Port 8080): OAuth2 Identity Provider
* **Redis** (Port 6379): Distributed caching (JSON)

### Core Services

| Service               | Port | Responsibility                   |
| --------------------- | ---- | -------------------------------- |
| User Service          | 8081 | User profile (sync với Keycloak) |
| Household Service     | 8082 | Nhà, phòng, thiết bị             |
| Worker Service        | 8083 | Thợ, skill mapping               |
| Service Order Service | 8084 | Workflow đơn hàng                |
| Payment Service       | 8085 | MoMo integration                 |

---

## 🔄 Order Lifecycle

```
POSTED → CLAIMED → IN_PROGRESS → COMPLETED → PAID
             ↘ CANCELLED
```

* State transition được kiểm soát chặt để đảm bảo consistency
* Thanh toán retry nếu fail tại COMPLETED

---

## ⚡ Caching Strategy (Redis)

* `ORDER_CACHE`: cache đơn hàng + sync bằng `@CachePut`
* `ORDER_ATTACHMENTS_LIST_CACHE`: cache list file, evict khi mutate
* `ORDER_REVIEW_CACHE`: cache review theo orderId
* `ROOM_CACHE`: evict khi soft delete
* `WORKER_SKILLS_LIST_CACHE`: evict khi update skill

👉 Sử dụng **Jackson JSON serializer (Spring Boot 4.x)**

---

## 🛡️ Security & Networking

* OAuth2 Resource Server (JWT từ Keycloak)
* `jwk-set-uri` nội bộ (Docker network)
* Timeout control:

    * Gateway: 2s connect / 5s response
    * Feign: configurable
    * Tomcat: 20s

---

## 🗄️ Database (Isolated per Service)

| Service   | DB           | Port |
| --------- | ------------ | ---- |
| Household | household_db | 5434 |
| Worker    | worker_db    | 5435 |
| Order     | order_db     | 5436 |
| Keycloak  | keycloak_db  | 5437 |
| User      | user_db      | 5438 |
| Payment   | payment_db   | 5439 |

---

## 🚀 Run System

### 1. Build

```bash
mvn clean package -DskipTests
```

### 2. Start Docker

```bash
docker-compose down --volumes --remove-orphans
docker-compose up --build -d
```

### 3. Check

```bash
docker-compose ps
```

---

## 🎯 API Access (via Gateway)

Base URL:

```
http://localhost:8000
```

| Feature   | Endpoint                         |
| --------- | -------------------------------- |
| User      | `/api/users/profile`             |
| Household | `/api/houses`, `/api/equipments` |
| Orders    | `/api/service-orders`            |
| Payments  | `/api/payments`                  |

Keycloak Admin:

```
http://localhost:8080
```

---

## 🛠️ Troubleshooting

Logs:

```bash
docker-compose logs -f <service-name>
```

DB lỗi schema:

* Drop table
* Restart container
* Hibernate auto recreate

---

## 📌 Tech Stack

* Spring Boot (Microservices)
* Spring Cloud Gateway
* Keycloak (OAuth2)
* Redis (Caching)
* PostgreSQL
* Docker Compose
* OpenFeign

---

## 🎯 Design Principles

* Service isolation (DB per service)
* Eventual consistency
* Cache-first read optimization
* Secure-by-default (OAuth2)
