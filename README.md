# Badminton Store API

Chào mừng bạn đến với dự án **Badminton Store API**. Đây là hệ thống backend cung cấp các API quản lý cho cửa hàng cầu lông, bao gồm các tính năng xác thực và phân quyền (dựa trên giao thức OAuth2).

## Công nghệ sử dụng (Tech Stack)

Dự án được xây dựng bằng các công nghệ và công cụ sau:

- **Ngôn ngữ**: Java 11
- **Framework**: Spring Boot 2.3.0.RELEASE (Spring Data JPA, Spring Security, OAuth2, OpenFeign)
- **Cơ sở dữ liệu**: MySQL
- **Quản lý phiên bản DB**: Liquibase
- **Message Broker**: RabbitMQ
- **Tài liệu API**: Swagger UI (Springfox)
- **Công cụ build**: Maven
- **Khác**: Docker, Lombok, MapStruct

## Cấu trúc dự án

- `source/mgr-api/`: Chứa mã nguồn chính của ứng dụng Spring Boot (Management API).
- `deploy/` & `dev-ops/`: Chứa các tài nguyên phục vụ cho việc triển khai dự án (Docker, k8s, v.v.).
- `init.txt`: Chứa script SQL khởi tạo user và database ban đầu (`db_badminton`).

## Hướng dẫn cài đặt và chạy dự án

### 1. Khởi tạo Cơ sở dữ liệu (MySQL)
Sử dụng các lệnh SQL sau (tham khảo file `init.txt`) để tạo cơ sở dữ liệu và người dùng:
```sql
CREATE DATABASE `db_badminton` CHARACTER SET utf8;
CREATE USER 'db_badminton'@'%' IDENTIFIED BY 'db_mgr_usr@123';
GRANT ALL PRIVILEGES ON db_badminton.* TO 'db_badminton'@'%';
FLUSH PRIVILEGES;
```

### 2. Cấu hình ứng dụng
Các cấu hình chính của ứng dụng nằm trong mã nguồn `source/mgr-api`. Bạn cần đảm bảo các thông số kết nối Database và RabbitMQ là chính xác với môi trường local của bạn (thông qua `application.yml` hoặc `application.properties`):
- **Cấu hình MySQL**: Kiểm tra URL kết nối, username và password.
- **Cấu hình RabbitMQ**: Kiểm tra host, port, username và password.

*(Lưu ý: Profile mặc định thường là `dev`, bạn có thể kiểm tra trực tiếp trong file `pom.xml` hoặc cấu hình Spring Boot).*

### 3. Build dự án bằng Maven
Di chuyển vào thư mục chứa source code và chạy lệnh build:
```bash
cd source/mgr-api
mvn clean package -DskipTests
```

### 4. Khởi chạy ứng dụng
Sau khi build thành công, bạn có thể khởi chạy file `.jar` vừa được tạo ra ở thư mục `target/`:
```bash
java -jar target/mgr-api-1.0.0.jar -Dspring.profiles.active=dev
```
Hoặc chạy trực tiếp bằng Spring Boot plugin:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## Khởi chạy với Docker

Bạn cũng có thể đóng gói và khởi chạy ứng dụng bằng Docker:

1. **Build Docker Image**:
   ```bash
   cd source/mgr-api
   docker build . --tag badminton-store-api:v1.0.0
   ```
2. **Run Docker Container**:
   ```bash
   docker run -it -p 8080:8080 -e "SPRING_PROFILES_ACTIVE=dev" badminton-store-api:v1.0.0
   ```

## Tài liệu API (Swagger UI)
Sau khi ứng dụng đã khởi chạy thành công, bạn có thể xem và tương tác với các API thông qua giao diện Swagger UI tại địa chỉ:
`http://localhost:8080/swagger-ui.html`

## Hỗ trợ
Nếu có bất kỳ vấn đề nào trong quá trình build và chạy dự án, vui lòng liên hệ với team phát triển phần mềm (Software Engineer) để được hỗ trợ.
