# Password Reset API Documentation

## API Endpoint

**POST** `/api/auth/reset-password`

## Description

API này cho phép người dùng yêu cầu reset mật khẩu bằng cách gửi email. Hệ thống sẽ tạo một mật khẩu mới ngẫu nhiên và gửi về email của người dùng.

## Request Body

```json
{
  "email": "user@example.com"
}
```

## Response

### Success Response (200 OK)

```json
{
  "message": "New password has been sent to your email address",
  "status": "success"
}
```

### Error Response (500 Internal Server Error)

```json
{
  "message": "Failed to reset password. Please try again later.",
  "status": "error"
}
```

## Features

1. **Không cần authentication**: API này có thể được gọi mà không cần token JWT
2. **Mật khẩu ngẫu nhiên**: Tạo mật khẩu mới với độ dài từ 6-40 ký tự
3. **Bảo mật**: Mật khẩu được mã hóa bằng BCrypt trước khi lưu vào database
4. **Email notification**: Gửi email với mật khẩu mới đến địa chỉ email được cung cấp

## Password Generation Rules

- Độ dài: 6-40 ký tự
- Bao gồm: chữ hoa, chữ thường, số, ký tự đặc biệt
- Đảm bảo có ít nhất 1 ký tự từ mỗi loại
- Các ký tự được trộn ngẫu nhiên

## Email Configuration

Để sử dụng API này, bạn cần cấu hình email trong `application.properties`:

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.ssl.trust=smtp.gmail.com
```

## Example Usage

### cURL

```bash
curl -X POST http://localhost:8080/api/auth/reset-password \
  -H "Content-Type: application/json" \
  -d '{"email": "user@example.com"}'
```

### JavaScript (Fetch)

```javascript
fetch("http://localhost:8080/api/auth/reset-password", {
  method: "POST",
  headers: {
    "Content-Type": "application/json",
  },
  body: JSON.stringify({
    email: "user@example.com",
  }),
})
  .then((response) => response.json())
  .then((data) => console.log(data));
```

## Security Notes

- API này không yêu cầu authentication vì người dùng chưa đăng nhập
- Mật khẩu mới được mã hóa trước khi lưu vào database
- Email được gửi với nội dung tiếng Anh theo yêu cầu
- Người dùng nên thay đổi mật khẩu sau lần đăng nhập đầu tiên
