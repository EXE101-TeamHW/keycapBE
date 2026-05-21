# Keycap Design Backend

Backend cho hệ thống bán keycap và nhận thiết kế keycap custom theo yêu cầu khách hàng. Dự án hỗ trợ đầy đủ các luồng chính của một cửa hàng keycap: khách hàng xem sản phẩm, đặt hàng, gửi yêu cầu thiết kế riêng, trao đổi realtime với staff, duyệt mockup, thanh toán qua PayOS và theo dõi tiến độ xử lý.

## Mục Tiêu Dự Án

Keycap Design Backend được xây dựng để phục vụ một nền tảng nơi khách hàng có thể mua keycap có sẵn hoặc đặt thiết kế keycap riêng theo ý tưởng cá nhân. Hệ thống không chỉ quản lý sản phẩm và đơn hàng, mà còn hỗ trợ quy trình làm việc giữa Customer, Staff và Admin trong quá trình tư vấn, thiết kế, duyệt mockup và sản xuất.

Các vai trò chính:

- `CUSTOMER`: xem sản phẩm, tạo giỏ hàng, đặt hàng, gửi yêu cầu custom, chat với staff, góp ý mockup.
- `STAFF`: tiếp nhận yêu cầu, trò chuyện với khách hàng, tạo mockup, cập nhật tiến độ xử lý.
- `ADMIN`: quản lý người dùng, sản phẩm, đơn hàng, phân công ticket cho staff.

## Tính Năng Chính

### Xác Thực Và Người Dùng

- Đăng ký, đăng nhập bằng email và password.
- JWT authentication cho các request cần xác thực.
- Xác thực email bằng mã verification code.
- Đăng nhập Google OAuth2.
- Quản lý role người dùng: `CUSTOMER`, `STAFF`, `ADMIN`.
- Quản lý trạng thái user: active, inactive, banned.

### Sản Phẩm Keycap

- Admin tạo và cập nhật sản phẩm keycap.
- Khách hàng xem danh sách sản phẩm.
- Hỗ trợ filter sản phẩm theo:
  - theme
  - layout
  - key profile
  - khoảng giá
- Quản lý tồn kho sản phẩm.
- Lưu ảnh sản phẩm dưới dạng JSON.
- Khách hàng có thể đánh giá sản phẩm sau khi mua.

### Giỏ Hàng Và Đơn Hàng

- Customer thêm sản phẩm vào giỏ hàng.
- Xem danh sách sản phẩm trong giỏ.
- Xóa sản phẩm khỏi giỏ.
- Tạo đơn hàng từ sản phẩm có sẵn.
- Tạo đơn hàng cho yêu cầu custom.
- Quản lý trạng thái đơn hàng:
  - `PENDING`
  - `CONFIRMED`
  - `SHIPPED`
  - `DELIVERED`
  - `CANCELLED`
- Hỗ trợ phương thức thanh toán:
  - `COD`
  - `BANK_TRANSFER`
  - `PAYOS`
  - `MOMO`

### Thiết Kế Keycap Custom

Customer có thể gửi yêu cầu thiết kế keycap riêng với các thông tin:

- tên thiết kế
- layout mong muốn
- theme
- ảnh tham khảo
- ghi chú chi tiết

Khi Customer tạo yêu cầu custom, hệ thống tự động tạo ticket để Admin hoặc Staff tiếp tục xử lý. Ticket giúp theo dõi toàn bộ vòng đời của yêu cầu thiết kế, từ lúc chờ duyệt cho đến khi hoàn thành.

Các trạng thái ticket bao gồm:

- `PENDING`
- `IN_REVIEW`
- `DESIGNING`
- `AWAITING_APPROVAL`
- `APPROVED`
- `REJECTED`
- `WAITING_PAYMENT`
- `PAID`
- `IN_PRODUCTION`
- `QUALITY_CHECK`
- `READY_SHIP`
- `SHIPPED`
- `DELIVERED`
- `COMPLETED`
- `CANCELLED`

### Ticket, Mockup Và Feedback

Admin có thể phân công ticket cho Staff phụ trách. Staff sau đó có thể tạo mockup thiết kế gửi khách hàng xem trước.

Hệ thống hỗ trợ:

- danh sách ticket
- chi tiết ticket
- phân công staff xử lý ticket
- cập nhật trạng thái ticket
- tạo mockup theo từng ticket
- xem danh sách mockup của ticket
- khách hàng gửi feedback cho mockup

Feedback có thể là:

- `COMMENT`: góp ý thông thường
- `REVISION`: yêu cầu chỉnh sửa
- `APPROVED`: xác nhận duyệt mockup

### Chat Realtime Customer - Staff

Dự án có module chat realtime để Customer trao đổi trực tiếp với Staff về yêu cầu custom keycap.

Đặc điểm:

- Chat 1-1 giữa Customer và Staff.
- Customer có thể bắt đầu cuộc trò chuyện mới.
- Staff có thể xem nhiều cuộc trò chuyện và phản hồi nhiều Customer.
- Tin nhắn được lưu vào SQL Server.
- Client nhận tin nhắn realtime qua WebSocket.
- Hỗ trợ trạng thái đã đọc/chưa đọc.
- Staff có thể đóng cuộc trò chuyện khi đã xử lý xong.

WebSocket sử dụng STOMP:

- endpoint: `/ws`
- client gửi tin nhắn qua: `/app/chat`
- client subscribe theo conversation: `/topic/chat/{conversationId}`

JWT cho WebSocket được truyền qua STOMP native header:

```text
Authorization: Bearer <token>
```

### Thanh Toán PayOS

Dự án tích hợp PayOS theo flow payment link và webhook:

1. Backend tạo payment URL từ order.
2. Frontend nhận `paymentUrl`.
3. Frontend redirect khách hàng sang trang thanh toán PayOS.
4. PayOS redirect về return URL hoặc cancel URL của backend để frontend đọc kết quả.
5. PayOS gửi webhook về backend.
6. Backend verify webhook bằng PayOS SDK.
7. Nếu thanh toán thành công, order được cập nhật `paymentStatus = PAID`.

Cấu hình PayOS nằm trong `application.properties`:

```properties
payos.client-id=
payos.api-key=
payos.checksum-key=
payos.return-url=http://localhost:8080/api/payments/payos/return
payos.cancel-url=http://localhost:8080/api/payments/payos/cancel
```

### Upload Ảnh

Dự án tích hợp Cloudinary để upload ảnh sản phẩm, ảnh tham khảo hoặc mockup.

- Endpoint upload nhận file multipart.
- Response trả về URL ảnh và public ID.
- URL ảnh có thể được lưu trong sản phẩm, custom request hoặc mockup.

### Báo Cáo

Backend có module report để hỗ trợ thống kê và theo dõi hoạt động kinh doanh, bao gồm dữ liệu doanh thu, xu hướng và hiệu suất nhân viên.

## Công Nghệ Sử Dụng

- Java 21
- Spring Boot 4
- Spring Web MVC
- Spring Data JPA
- Spring Security
- JWT
- Spring WebSocket/STOMP
- SQL Server
- Maven
- Lombok
- Cloudinary
- Google OAuth2
- PayOS
- Springdoc OpenAPI/Swagger

## Cấu Trúc Package

```text
com.keycap.keycapdesign
├── common        # response wrapper dùng chung
├── config        # cấu hình password, cloudinary
├── controller    # REST API controllers
├── dto           # request/response DTO
├── entity        # JPA entities
├── enums         # enum trạng thái, role, payment, layout
├── exception     # custom exception và global handler
├── repository    # Spring Data JPA repositories
├── security      # JWT, filter, user principal, security config
├── service       # business logic
├── util          # helper utilities
└── websocket     # WebSocket config, auth interceptor, chat controller
```

## Database Chính

Dự án sử dụng SQL Server. Các nhóm bảng quan trọng:

- `users`: thông tin tài khoản và role.
- `products`: sản phẩm keycap có sẵn.
- `cart_items`: giỏ hàng của customer.
- `orders`, `order_items`: đơn hàng và chi tiết đơn hàng.
- `custom_requests`: yêu cầu thiết kế keycap custom.
- `tickets`: ticket xử lý yêu cầu custom.
- `mockups`: file mockup do staff gửi.
- `mockup_feedbacks`: feedback của customer cho mockup.
- `conversations`: cuộc trò chuyện Customer - Staff.
- `messages`: tin nhắn trong conversation.
- `reviews`: đánh giá sản phẩm.
- `email_verification_codes`: mã xác thực email.

## Luồng Nghiệp Vụ Tổng Quan

### Luồng Mua Sản Phẩm Có Sẵn

1. Customer đăng ký hoặc đăng nhập.
2. Customer xem danh sách sản phẩm keycap.
3. Customer thêm sản phẩm vào giỏ hàng.
4. Customer tạo order.
5. Nếu chọn PayOS, backend trả về `paymentUrl`.
6. Frontend redirect sang PayOS để thanh toán.
7. Sau thanh toán, backend cập nhật trạng thái thanh toán.
8. Admin hoặc Staff cập nhật trạng thái giao hàng.

### Luồng Đặt Thiết Kế Custom

1. Customer tạo custom request với mô tả thiết kế.
2. Hệ thống tự động tạo ticket.
3. Admin phân công ticket cho Staff.
4. Staff trao đổi thêm với Customer qua chat realtime.
5. Staff tạo mockup gửi Customer.
6. Customer gửi feedback hoặc approve mockup.
7. Khi thiết kế được duyệt, hệ thống tạo order custom.
8. Customer thanh toán cọc hoặc thanh toán toàn bộ.
9. Staff/Admin cập nhật tiến độ sản xuất và giao hàng.

### Luồng Chat Realtime

1. Customer tạo conversation.
2. Customer và Staff kết nối WebSocket bằng JWT.
3. Hai bên subscribe vào topic của conversation.
4. Tin nhắn gửi qua `/app/chat`.
5. Backend lưu tin nhắn vào database.
6. Backend publish tin nhắn realtime tới `/topic/chat/{conversationId}`.
7. Người nhận đánh dấu đã đọc.
8. Staff đóng conversation khi đã xử lý xong.

## Cấu Hình Chạy Local

### Yêu Cầu

- Java 21
- Maven hoặc Maven Wrapper
- SQL Server local

### SQL Server

Cập nhật file:

```text
src/main/resources/application.properties
```

Ví dụ:

```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=keycap_design;encrypt=true;trustServerCertificate=true
spring.datasource.username=sa
spring.datasource.password=123456
spring.jpa.hibernate.ddl-auto=update
```

### JWT

`jwt.secret` nên có tối thiểu 32 ký tự:

```properties
jwt.secret=change_this_secret_key_32chars_minimum
jwt.expiration-ms=3600000
```

### Google OAuth2

```properties
spring.security.oauth2.client.registration.google.client-id=
spring.security.oauth2.client.registration.google.client-secret=
spring.security.oauth2.client.registration.google.scope=openid,profile,email
```

### Cloudinary

```properties
cloudinary.cloud-name=
cloudinary.api-key=
cloudinary.api-secret=
```

### Mail

```properties
mail.enabled=true
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=
spring.mail.password=
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

## Chạy Dự Án

Windows:

```cmd
mvnw.cmd spring-boot:run
```

Nếu Maven Wrapper lỗi, có thể dùng Maven đã cài sẵn:

```cmd
mvn spring-boot:run
```

Chạy test:

```cmd
mvn test
```

## Swagger

Sau khi chạy backend:

```text
http://localhost:8080/swagger-ui.html
```

OpenAPI JSON:

```text
http://localhost:8080/v3/api-docs
```

## Ghi Chú Phát Triển

- Backend hiện vẫn cho phép nhiều API truyền `userId` trong request để thuận tiện test và demo.
- JWT đã được dùng cho HTTP filter và WebSocket STOMP connection.
- WebSocket yêu cầu token trong native header `Authorization`.
- JPA đang dùng `ddl-auto=update`, phù hợp giai đoạn development.
- Khi triển khai thật, nên siết lại phân quyền trong `SecurityConfig` theo role cho từng nhóm API.
- Không commit secret thật của SQL Server, JWT, Cloudinary, Google OAuth2 hoặc PayOS lên repository.
