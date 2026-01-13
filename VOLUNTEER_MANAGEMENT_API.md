# Volunteer Management API Documentation

## Overview

API CRUD quản lý volunteer dành cho Admin với các tính năng tìm kiếm, phân trang, và xóa mềm.

## Base URL

```
http://localhost:8080/api/admin
```

## Authentication

Tất cả API yêu cầu JWT token với role `ROLE_ADMIN`:

```
Authorization: Bearer YOUR_JWT_TOKEN
```

---

## 📋 **1. GET /volunteers - Lấy danh sách volunteers**

### Description

Lấy danh sách tất cả volunteers với tìm kiếm (không phân trang).

### Query Parameters

| Parameter | Type   | Required | Default | Description                             |
| --------- | ------ | -------- | ------- | --------------------------------------- |
| search    | String | No       | -       | Tìm kiếm theo tên hoặc email (gần đúng) |

### Example Request

```http
GET /api/admin/volunteers?search=john
```

### Response

```json
[
  {
    "id": 1,
    "fullName": "John Doe",
    "email": "john@example.com",
    "contact": "0123456789",
    "pic": "profile.jpg",
    "isActive": true,
    "isBanned": false,
    "role": "ROLE_USER",
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00",
    "isDeleted": false,
    "organizationName": "Charity Org",
    "organizationId": 1
  },
  {
    "id": 2,
    "fullName": "Jane Smith",
    "email": "jane@example.com",
    "contact": "0987654321",
    "pic": "profile2.jpg",
    "isActive": true,
    "isBanned": false,
    "role": "ROLE_ORGANIZATION",
    "createdAt": "2024-01-02T10:00:00",
    "updatedAt": "2024-01-02T10:00:00",
    "isDeleted": false,
    "organizationName": null,
    "organizationId": null
  }
]
```

---

## 🔍 **2. GET /volunteers/{id} - Lấy chi tiết volunteer**

### Description

Lấy thông tin chi tiết của volunteer bao gồm organization, events đã tham gia, và lịch sử donate.

### Path Parameters

| Parameter | Type | Required | Description      |
| --------- | ---- | -------- | ---------------- |
| id        | Long | Yes      | ID của volunteer |

### Example Request

```http
GET /api/admin/volunteers/1
```

### Response

```json
{
  "id": 1,
  "fullName": "John Doe",
  "email": "john@example.com",
  "contact": "0123456789",
  "pic": "profile.jpg",
  "isActive": true,
  "isBanned": false,
  "role": "ROLE_USER",
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T10:00:00",
  "deletedAt": null,
  "isDeleted": false,
  "organization": {
    "id": 1,
    "organizationName": "Charity Org",
    "description": "A charity organization",
    "volunteerId": 1
  },
  "participatedEvents": [
    {
      "id": 1,
      "charityName": "Food Drive",
      "description": "Collecting food for homeless",
      "dateStart": "2024-01-15T09:00:00",
      "dateEnd": "2024-01-15T17:00:00",
      "eventStatus": "completed",
      "joinStatus": "COMPLETED"
    }
  ],
  "donationHistory": [
    {
      "id": 1,
      "donateAmount": 100.0,
      "note": "Donation for flood victims",
      "createdAt": "2024-01-10T14:30:00",
      "donationEventTitle": "Flood Relief Fund"
    }
  ],
  "totalEventsParticipated": 1,
  "totalDonationAmount": 100.0,
  "totalDonationCount": 1
}
```

---

## ➕ **3. POST /volunteers - Tạo volunteer mới**

### Description

Tạo volunteer mới với account tương ứng.

### Request Body

```json
{
  "email": "newuser@example.com",
  "password": "password123",
  "fullName": "New User",
  "contact": "0123456789",
  "role": "ROLE_USER"
}
```

### Response

```json
{
  "id": 2,
  "fullName": "New User",
  "email": "newuser@example.com",
  "contact": "0123456789",
  "pic": "",
  "isActive": true,
  "isBanned": false,
  "role": "ROLE_USER",
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T10:00:00",
  "isDeleted": false,
  "organizationName": null,
  "organizationId": null
}
```

---

## ✏️ **4. PUT /volunteers/{id} - Cập nhật volunteer**

### Description

Cập nhật thông tin volunteer.

### Path Parameters

| Parameter | Type | Required | Description      |
| --------- | ---- | -------- | ---------------- |
| id        | Long | Yes      | ID của volunteer |

### Request Body

```json
{
  "email": "updated@example.com",
  "fullName": "Updated Name",
  "contact": "0987654321",
  "role": "ROLE_ORGANIZATION",
  "isActive": true,
  "isBanned": false
}
```

### Response

```json
{
  "id": 1,
  "fullName": "Updated Name",
  "email": "updated@example.com",
  "contact": "0987654321",
  "pic": "profile.jpg",
  "isActive": true,
  "isBanned": false,
  "role": "ROLE_ORGANIZATION",
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T11:00:00",
  "isDeleted": false,
  "organizationName": "Charity Org",
  "organizationId": 1
}
```

---

## 🗑️ **5. DELETE /volunteers/{id} - Xóa mềm volunteer**

### Description

Xóa mềm volunteer (set isDeleted = true, isActive = false).

### Path Parameters

| Parameter | Type | Required | Description      |
| --------- | ---- | -------- | ---------------- |
| id        | Long | Yes      | ID của volunteer |

### Response

```json
{
  "message": "Volunteer deleted successfully"
}
```

---

## 🚫 **6. PUT /volunteers/{id}/ban - Ban/Unban volunteer**

### Description

Ban hoặc unban volunteer.

### Path Parameters

| Parameter | Type | Required | Description      |
| --------- | ---- | -------- | ---------------- |
| id        | Long | Yes      | ID của volunteer |

### Query Parameters

| Parameter | Type    | Required | Description                 |
| --------- | ------- | -------- | --------------------------- |
| banned    | Boolean | Yes      | true để ban, false để unban |

### Example Request

```http
PUT /api/admin/volunteers/1/ban?banned=true
```

### Response

```json
{
  "message": "Volunteer banned successfully"
}
```

---

## 🔄 **7. PUT /volunteers/{id}/status - Kích hoạt/Vô hiệu hóa account**

### Description

Kích hoạt hoặc vô hiệu hóa account của volunteer.

### Path Parameters

| Parameter | Type | Required | Description      |
| --------- | ---- | -------- | ---------------- |
| id        | Long | Yes      | ID của volunteer |

### Query Parameters

| Parameter | Type    | Required | Description                             |
| --------- | ------- | -------- | --------------------------------------- |
| active    | Boolean | Yes      | true để kích hoạt, false để vô hiệu hóa |

### Example Request

```http
PUT /api/admin/volunteers/1/status?active=false
```

### Response

```json
{
  "message": "Volunteer deactivated successfully"
}
```

---

## 👑 **8. PUT /volunteers/{id}/role - Thay đổi role**

### Description

Thay đổi role của volunteer.

### Path Parameters

| Parameter | Type | Required | Description      |
| --------- | ---- | -------- | ---------------- |
| id        | Long | Yes      | ID của volunteer |

### Query Parameters

| Parameter | Type   | Required | Description                                   |
| --------- | ------ | -------- | --------------------------------------------- |
| role      | String | Yes      | ROLE_USER, ROLE_ORGANIZATION, hoặc ROLE_ADMIN |

### Example Request

```http
PUT /api/admin/volunteers/1/role?role=ROLE_ORGANIZATION
```

### Response

```json
{
  "message": "Volunteer role updated successfully"
}
```

---

## 📝 **Test Cases với Postman**

### 1. Tạo Admin Account

```json
POST /api/auth/signup
{
  "email": "admin@test.com",
  "password": "admin123",
  "fullName": "Admin User",
  "role": "ROLE_ADMIN"
}
```

### 2. Đăng nhập Admin

```json
POST /api/auth/signin
{
  "email": "admin@test.com",
  "password": "admin123"
}
```

### 3. Test các API với token từ bước 2

- Lấy danh sách volunteers (không phân trang)
- Tạo volunteer mới
- Cập nhật volunteer
- Xem chi tiết volunteer
- Ban/unban volunteer
- Thay đổi role

---

## ⚠️ **Lưu ý quan trọng**

1. **Authentication**: Tất cả API yêu cầu JWT token với role ADMIN
2. **Soft Delete**: DELETE API chỉ xóa mềm, không xóa thật khỏi database
3. **Search**: Tìm kiếm theo tên hoặc email (case-insensitive)
4. **No Pagination**: API list trả về tất cả volunteers (phù hợp với data ít)
5. **Validation**: Email phải unique, password tối thiểu 6 ký tự
6. **Role Management**: Có thể thay đổi role giữa USER, ORGANIZATION, ADMIN

---

## 🔧 **Error Handling**

### Common Error Responses

```json
{
  "error": "Volunteer not found with id: 999",
  "status": 404
}
```

```json
{
  "error": "Email already exists: existing@example.com",
  "status": 400
}
```

```json
{
  "error": "Access Denied",
  "status": 403
}
```
