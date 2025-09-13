# Organization Management API Documentation

## Overview

This document describes the Organization Management APIs for the Volunteer Hub system. These APIs allow administrators to manage organizations, including creating, reading, updating, and deleting organization records.

## Authentication

All APIs require admin authentication. Include the JWT token in the Authorization header:

```
Authorization: Bearer <your-jwt-token>
```

## Base URL

```
http://localhost:8080/api/admin
```

---

## API Endpoints

### User Organization Creation

**POST** `/api/organizations`

Create a new organization as a regular user. The owner (volunteer) must already exist in the system. The request will be created with `PENDING` status and requires admin approval.

#### Request Body

```json
{
  "organizationName": "My Organization",
  "description": "My organization description",
  "certificate": "certificate_url",
  "logo": "logo_url",
  "volunteerId": 456
}
```

#### Request Body Fields

| Field            | Type   | Required | Description                                        |
| ---------------- | ------ | -------- | -------------------------------------------------- |
| organizationName | String | Yes      | Name of the organization                           |
| description      | String | Yes      | Description of the organization                    |
| certificate      | String | No       | URL to organization certificate                    |
| logo             | String | No       | URL to organization logo                           |
| volunteerId      | Long   | Yes      | ID of the volunteer who will own this organization |

#### Response

```json
{
  "id": 3,
  "organizationName": "My Organization",
  "description": "My organization description",
  "certificate": "certificate_url",
  "logo": "logo_url",
  "createdAt": "2024-01-25T10:30:00",
  "updatedAt": "2024-01-25T10:30:00",
  "deleted": false,
  "ownerName": "Current User",
  "ownerEmail": "user@example.com",
  "ownerContact": "+84123456789",
  "ownerStatus": null,
  "ownerIsActive": true,
  "ownerIsBanned": false
}
```

#### Example Request

```bash
POST /api/organizations
Content-Type: application/json
Authorization: Bearer <user-jwt-token>

{
  "organizationName": "My Organization",
  "description": "My organization description",
  "volunteerId": 456
}
```

---

## Admin API Endpoints

### 1. Get All Organizations

**GET** `/api/admin/organizations`

Get a list of all organizations with approved requests and optional search functionality. Only organizations with approved requests and not deleted will be returned.

#### Query Parameters

| Parameter | Type   | Required | Description                                                                                                         |
| --------- | ------ | -------- | ------------------------------------------------------------------------------------------------------------------- |
| search    | String | No       | Search organizations by name (case-insensitive). Only returns organizations with approved requests and not deleted. |

#### Response

```json
[
  {
    "id": 1,
    "organizationName": "Red Cross Vietnam",
    "description": "Humanitarian organization providing emergency assistance",
    "certificate": "certificate_url",
    "logo": "logo_url",
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00",
    "deleted": false,
    "ownerName": "John Doe",
    "ownerEmail": "john.doe@redcross.org",
    "ownerContact": "+84123456789",
    "ownerStatus": "ACTIVE",
    "ownerIsActive": true,
    "ownerIsBanned": false
  }
]
```

#### Example Requests

```bash
# Get all organizations with approved requests
GET /api/admin/organizations

# Search organizations by name (only approved ones)
GET /api/admin/organizations?search=red cross
```

**Note**: Only organizations with approved requests and not deleted will be returned in the response.

---

### 2. Get Organization Detail

**GET** `/api/admin/organizations/{id}`

Get detailed information about a specific organization including statistics and event lists.

#### Path Parameters

| Parameter | Type | Required | Description     |
| --------- | ---- | -------- | --------------- |
| id        | Long | Yes      | Organization ID |

#### Response

```json
{
  "id": 1,
  "organizationName": "Red Cross Vietnam",
  "description": "Humanitarian organization providing emergency assistance",
  "certificate": "certificate_url",
  "logo": "logo_url",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00",
  "deleted": false,
  "ownerName": "John Doe",
  "ownerEmail": "john.doe@redcross.org",
  "ownerContact": "+84123456789",
  "ownerStatus": "ACTIVE",
  "ownerIsActive": true,
  "ownerIsBanned": false,
  "totalVolunteersParticipated": 150,
  "totalDonationAmount": 50000000,
  "totalCharityEvents": 5,
  "totalDonationEvents": 3,
  "charityEvents": [
    {
      "id": 1,
      "charityName": "Flood Relief Campaign",
      "organizationId": 1,
      "description": "Emergency relief for flood victims",
      "destination": "Central Vietnam",
      "dateStart": "2024-02-01T08:00:00",
      "dateEnd": "2024-02-15T18:00:00",
      "numVolunteerRequire": 50,
      "numVolunteerActual": 45,
      "note": "Urgent response needed",
      "pic": "event_image_url",
      "eventStatus": "ONGOING",
      "joinStatus": null
    }
  ],
  "donationEvents": [
    {
      "id": 1,
      "donationName": "Medical Equipment Fund",
      "organizationId": 1,
      "description": "Fundraising for medical equipment",
      "destination": "Local hospitals",
      "dateStart": "2024-01-20T00:00:00",
      "dateEnd": "2024-03-20T23:59:59",
      "targetAmount": 100000000,
      "actualAmount": 75000000,
      "note": "Critical medical supplies needed",
      "pic": "donation_image_url",
      "eventStatus": "ONGOING",
      "createdAt": "2024-01-20T10:00:00",
      "updatedAt": "2024-01-20T10:00:00"
    }
  ],
  "activeVolunteers": [
    {
      "id": 1,
      "fullName": "Alice Johnson",
      "contact": "+84123456789",
      "email": "alice@example.com",
      "isActive": true,
      "isBanned": false
    },
    {
      "id": 2,
      "fullName": "Bob Smith",
      "contact": "+84987654321",
      "email": "bob@example.com",
      "isActive": true,
      "isBanned": false
    }
  ]
}
```

#### Example Request

```bash
GET /api/admin/organizations/1
```

---

### 3. Create Organization

**POST** `/api/admin/organizations`

Create a new organization. The owner (volunteer) must already exist in the system. When admin creates an organization, the associated request is automatically approved.

#### Request Body

```json
{
  "organizationName": "Green Earth Foundation",
  "description": "Environmental protection and conservation organization",
  "certificate": "certificate_url",
  "logo": "logo_url",
  "volunteerId": 123
}
```

#### Request Body Fields

| Field            | Type   | Required | Description                                        |
| ---------------- | ------ | -------- | -------------------------------------------------- |
| organizationName | String | Yes      | Name of the organization                           |
| description      | String | Yes      | Description of the organization                    |
| certificate      | String | No       | URL to organization certificate                    |
| logo             | String | No       | URL to organization logo                           |
| volunteerId      | Long   | Yes      | ID of the volunteer who will own this organization |

#### Response

```json
{
  "id": 2,
  "organizationName": "Green Earth Foundation",
  "description": "Environmental protection and conservation organization",
  "certificate": "certificate_url",
  "logo": "logo_url",
  "createdAt": "2024-01-20T14:30:00",
  "updatedAt": "2024-01-20T14:30:00",
  "deleted": false,
  "ownerName": "Jane Smith",
  "ownerEmail": "admin@greenearth.org",
  "ownerContact": "+84987654321",
  "ownerStatus": "ACTIVE",
  "ownerIsActive": true,
  "ownerIsBanned": false
}
```

#### Example Request

```bash
POST /api/admin/organizations
Content-Type: application/json

{
  "organizationName": "Green Earth Foundation",
  "description": "Environmental protection and conservation organization",
  "certificate": "certificate_url",
  "logo": "logo_url",
  "volunteerId": 123
}
```

---

### 4. Update Organization

**PUT** `/api/admin/organizations/{id}`

Update an existing organization's information.

#### Path Parameters

| Parameter | Type | Required | Description     |
| --------- | ---- | -------- | --------------- |
| id        | Long | Yes      | Organization ID |

#### Request Body

```json
{
  "organizationName": "Updated Organization Name",
  "description": "Updated description",
  "certificate": "updated_certificate_url",
  "logo": "updated_logo_url"
}
```

#### Request Body Fields

| Field            | Type   | Required | Description               |
| ---------------- | ------ | -------- | ------------------------- |
| organizationName | String | No       | Updated organization name |
| description      | String | No       | Updated description       |
| certificate      | String | No       | Updated certificate URL   |
| logo             | String | No       | Updated logo URL          |

#### Response

```json
{
  "id": 1,
  "organizationName": "Updated Organization Name",
  "description": "Updated description",
  "certificate": "updated_certificate_url",
  "logo": "updated_logo_url",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-20T16:45:00",
  "deleted": false,
  "ownerName": "John Doe",
  "ownerEmail": "john.doe@redcross.org",
  "ownerContact": "+84123456789",
  "ownerStatus": "ACTIVE",
  "ownerIsActive": true,
  "ownerIsBanned": false
}
```

#### Example Request

```bash
PUT /api/admin/organizations/1
Content-Type: application/json

{
  "organizationName": "Red Cross Vietnam - Updated",
  "description": "Updated humanitarian organization description"
}
```

---

### 5. Delete Organization (Soft Delete)

**DELETE** `/api/admin/organizations/{id}`

Soft delete an organization (marks as deleted and sets deleted_at timestamp but doesn't remove from database).

#### Path Parameters

| Parameter | Type | Required | Description     |
| --------- | ---- | -------- | --------------- |
| id        | Long | Yes      | Organization ID |

#### Response

```json
{
  "message": "Organization deleted successfully"
}
```

#### Example Request

```bash
DELETE /api/admin/organizations/1
```

---

## Business Logic Notes

### Organization Creation Flow

1. **Admin Creates Organization**: When an admin creates an organization:

   - The owner (volunteer) must already exist in the system
   - The organization is created and linked to the existing volunteer
   - A request is automatically created with `APPROVED` status
   - No new account or volunteer is created

2. **User Creates Organization**: When a user creates their own organization (handled by user controller):
   - The organization is created and linked to the existing volunteer
   - A request is created with `PENDING` status
   - Admin must approve the request for the organization to be active
   - No new account or volunteer is created

### Statistics Calculation

The detail API provides comprehensive statistics:

- **Total Volunteers Participated**: Count of unique volunteers who participated in any charity event organized by this organization
- **Total Donation Amount**: Sum of all donations received across all donation events
- **Total Charity Events**: Count of all charity events created by this organization
- **Total Donation Events**: Count of all donation events created by this organization

### Event Lists

- **Charity Events**: List of all charity events with volunteer participation counts
- **Donation Events**: List of all donation events with actual donation amounts

### Active Volunteers

- **Active Volunteers**: List of all volunteers with active accounts in the system
- Includes volunteer ID, full name, contact information, email, and status
- Only volunteers with `isActive = true` are included

---

## Error Responses

### 400 Bad Request

```json
{
  "message": "Validation failed",
  "errors": ["Organization name is required", "Invalid email format"]
}
```

### 401 Unauthorized

```json
{
  "message": "Unauthorized access"
}
```

### 403 Forbidden

```json
{
  "message": "Access denied. Admin role required."
}
```

### 404 Not Found

```json
{
  "message": "Organization not found with id: 999"
}
```

### 500 Internal Server Error

```json
{
  "message": "Internal server error occurred"
}
```

---

## Testing Examples

### Test Case 1: Create Organization

```bash
curl -X POST http://localhost:8080/api/admin/organizations \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "organizationName": "Test Organization",
    "description": "Test organization for API testing",
    "certificate": "certificate_url",
    "logo": "logo_url",
    "volunteerId": 123
  }'
```

### Test Case 2: Get Organization Detail

```bash
curl -X GET http://localhost:8080/api/admin/organizations/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Test Case 3: Search Organizations

```bash
curl -X GET "http://localhost:8080/api/admin/organizations?search=red" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Test Case 4: Update Organization

```bash
curl -X PUT http://localhost:8080/api/admin/organizations/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "organizationName": "Updated Test Organization",
    "description": "Updated description for testing"
  }'
```

### Test Case 5: Delete Organization

```bash
curl -X DELETE http://localhost:8080/api/admin/organizations/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

## Notes

- All timestamps are in ISO 8601 format
- All monetary amounts are in VND (Vietnamese Dong)
- Soft delete means the record is marked as deleted but remains in the database
- Admin-created organizations are automatically approved
- User-created organizations require admin approval
- Search is case-insensitive and uses partial matching
- **Important**: The owner (volunteer) must already exist in the system before creating an organization
- No new accounts or volunteers are created during organization creation
- Role changes (to ROLE_ORGANIZATION) happen during the approval process, not during creation
- **List API**: Only organizations with approved requests and not deleted are returned in the list
- Organizations with pending, rejected requests, or deleted organizations will not appear in the list
- **Soft Delete**: When deleting an organization, both `is_deleted` is set to true and `deleted_at` is set to current timestamp
