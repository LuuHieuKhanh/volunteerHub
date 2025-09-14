# Request Management API Documentation

## Overview

This document describes the Request Management APIs for the Volunteer Hub system. These APIs allow administrators to manage organization registration requests and other types of requests.

## Base URL

```
http://localhost:8080/api/admin/requests
```

---

## API Endpoints

### 0. Admin Dashboard

**Endpoint:** `GET /api/admin/dashboard`

**Description:** Get comprehensive dashboard data for admin including system statistics and pending requests.

#### Response

**Success Response (200 OK):**

```json
{
  "totalVolunteers": 150,
  "totalOrganizations": 25,
  "totalCharityEvents": 45,
  "totalDonationEvents": 30,
  "totalPendingRequests": 8,
  "pendingRequests": [
    {
      "id": 1,
      "requestType": "ORGANIZATION_REGISTRATION",
      "status": "PENDING",
      "denyReason": null,
      "createdAt": "2024-01-15T10:00:00",
      "updatedAt": "2024-01-15T10:00:00",
      "organizationId": 1,
      "organizationName": "Green Earth Foundation",
      "organizationDescription": "Environmental protection organization",
      "organizationContact": "",
      "organizationAddress": "",
      "volunteerId": 1,
      "volunteerFullName": "John Doe",
      "volunteerContact": "+84987654321",
      "volunteerEmail": "john.doe@example.com"
    },
    {
      "id": 2,
      "requestType": "ORGANIZATION_REGISTRATION",
      "status": "PENDING",
      "denyReason": null,
      "createdAt": "2024-01-14T09:30:00",
      "updatedAt": "2024-01-14T09:30:00",
      "organizationId": 2,
      "organizationName": "Children's Hope",
      "organizationDescription": "Supporting underprivileged children",
      "organizationContact": "",
      "organizationAddress": "",
      "volunteerId": 2,
      "volunteerFullName": "Jane Smith",
      "volunteerContact": "+84444555666",
      "volunteerEmail": "jane.smith@example.com"
    }
  ]
}
```

#### Example Request

```
GET /api/admin/dashboard
```

---

## API Endpoints

### 1. Get All Requests

**Endpoint:** `GET /api/admin/requests`

**Description:** Get a list of all requests with optional search functionality by organization name.

#### Query Parameters

| Parameter | Type   | Required | Description                                              |
| --------- | ------ | -------- | -------------------------------------------------------- |
| search    | String | No       | Search requests by organization name (case-insensitive). |

#### Response

**Success Response (200 OK):**

```json
[
  {
    "id": 1,
    "requestType": "ORGANIZATION_REGISTRATION",
    "status": "PENDING",
    "denyReason": null,
    "createdAt": "2024-01-15T10:00:00",
    "updatedAt": "2024-01-15T10:00:00",
    "organizationId": 1,
    "organizationName": "Green Earth Foundation",
    "organizationDescription": "Environmental protection organization",
    "organizationContact": "",
    "organizationAddress": "",
    "volunteerId": 1,
    "volunteerFullName": "John Doe",
    "volunteerContact": "+84987654321",
    "volunteerEmail": "john.doe@example.com"
  },
  {
    "id": 2,
    "requestType": "ORGANIZATION_REGISTRATION",
    "status": "APPROVED",
    "denyReason": null,
    "createdAt": "2024-01-14T09:30:00",
    "updatedAt": "2024-01-14T15:45:00",
    "organizationId": 2,
    "organizationName": "Children's Hope",
    "organizationDescription": "Supporting underprivileged children",
    "organizationContact": "",
    "organizationAddress": "",
    "volunteerId": 2,
    "volunteerFullName": "Jane Smith",
    "volunteerContact": "+84444555666",
    "volunteerEmail": "jane.smith@example.com"
  }
]
```

#### Example Request

```
GET /api/admin/requests?search=Green
```

---

### 2. Get Request Detail

**Endpoint:** `GET /api/admin/requests/{id}`

**Description:** Get detailed information about a specific request including full organization and volunteer details.

#### Path Parameters

| Parameter | Type | Required | Description           |
| --------- | ---- | -------- | --------------------- |
| id        | Long | Yes      | The ID of the request |

#### Response

**Success Response (200 OK):**

```json
{
  "id": 1,
  "requestType": "ORGANIZATION_REGISTRATION",
  "status": "PENDING",
  "denyReason": null,
  "createdAt": "2024-01-15T10:00:00",
  "updatedAt": "2024-01-15T10:00:00",
  "organizationId": 1,
  "organizationName": "Green Earth Foundation",
  "organizationDescription": "Environmental protection organization",
  "organizationContact": "",
  "organizationAddress": "",
  "organizationEmail": "",
  "organizationWebsite": "",
  "organizationLogo": "logo_url",
  "organizationCreatedAt": "2024-01-15T10:00:00",
  "organizationUpdatedAt": "2024-01-15T10:00:00",
  "volunteerId": 1,
  "volunteerFullName": "John Doe",
  "volunteerContact": "+84987654321",
  "volunteerEmail": "john.doe@example.com",
  "volunteerAddress": "",
  "volunteerAvatar": "avatar_url",
  "volunteerCreatedAt": "2024-01-10T08:00:00",
  "volunteerUpdatedAt": "2024-01-15T10:00:00"
}
```

#### Example Request

```
GET /api/admin/requests/1
```

---

### 3. Update Request Status

**Endpoint:** `PUT /api/admin/requests/{id}/status`

**Description:** Update the status of a request. Supports two cases:

- **Approve**: Changes status to APPROVED and clears deny reason
- **Reject**: Changes status to REJECTED and requires deny reason

#### Path Parameters

| Parameter | Type | Required | Description           |
| --------- | ---- | -------- | --------------------- |
| id        | Long | Yes      | The ID of the request |

#### Request Body

```json
{
  "status": "APPROVED",
  "denyReason": null
}
```

**For Rejection:**

```json
{
  "status": "REJECTED",
  "denyReason": "Incomplete documentation provided"
}
```

#### Request Body Fields

| Field      | Type   | Required | Description                                               |
| ---------- | ------ | -------- | --------------------------------------------------------- |
| status     | String | Yes      | New status: "APPROVED" or "REJECTED"                      |
| denyReason | String | No       | Required when status is "REJECTED". Reason for rejection. |

#### Response

**Success Response (200 OK):**

```json
{
  "message": "Request approved successfully"
}
```

**For Rejection:**

```json
{
  "message": "Request rejected successfully"
}
```

#### Example Request

```
PUT /api/admin/requests/1/status
Content-Type: application/json

{
  "status": "APPROVED"
}
```

**Rejection Example:**

```
PUT /api/admin/requests/1/status
Content-Type: application/json

{
  "status": "REJECTED",
  "denyReason": "Organization documentation is incomplete"
}
```

---

## Business Logic Notes

### Admin Dashboard

- **Total Volunteers**: Count of all volunteers in the system
- **Total Organizations**: Count of all organizations in the system
- **Total Charity Events**: Count of all charity events in the system
- **Total Donation Events**: Count of all donation events in the system
- **Total Pending Requests**: Count of all requests with PENDING status
- **Pending Requests List**: Complete list of all pending requests with full details (no pagination, no search filter)

### Request Status Management

- **PENDING**: Initial status when request is created
- **APPROVED**: Request has been approved by admin
- **REJECTED**: Request has been rejected by admin

### Approval Process

- When a request is **APPROVED**:

  - Status is changed to APPROVED
  - Deny reason is cleared (set to null)
  - For organization registration requests, the volunteer's role is updated to ROLE_ORGANIZATION

- When a request is **REJECTED**:
  - Status is changed to REJECTED
  - Deny reason is required and must be provided
  - No role changes are made

### Search Functionality

- The search parameter filters requests by organization name (case-insensitive)
- If no search parameter is provided, all requests are returned
- Search uses partial matching (contains)

### Data Relationships

- Each request is linked to a volunteer (requestor) and an organization
- Organization information is included in the response for context
- Volunteer information includes account details (email) when available

---

## Error Responses

### 400 Bad Request

```json
{
  "message": "Deny reason is required when rejecting a request"
}
```

### 404 Not Found

```json
{
  "message": "Request not found with id: 1"
}
```

### 500 Internal Server Error

```json
{
  "message": "Internal server error occurred"
}
```

---

## Request Types

The system supports the following request types:

- **ORGANIZATION_REGISTRATION**: Request to register a new organization

---

## Request Status Values

- **PENDING**: Request is waiting for admin review
- **APPROVED**: Request has been approved
- **REJECTED**: Request has been rejected

---

## Notes

- **List API**: Returns all requests with optional search by organization name
- **Detail API**: Provides comprehensive information about a specific request
- **Status Update**: Supports both approval and rejection with appropriate business logic
- **Role Management**: Organization registration approval automatically updates volunteer role to ROLE_ORGANIZATION
- **Search**: Case-insensitive partial matching on organization name
