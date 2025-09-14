# Donation Event Management API Documentation

## Overview

This document describes the Donation Event Management APIs for the Volunteer Hub system. These APIs allow administrators to manage donation events including creating, updating, deleting, and monitoring donation activities.

## Base URL

```
http://localhost:8080/api/admin/donation-events
```

---

## API Endpoints

### 0. Get Volunteer Donation History

**Endpoint:** `GET /api/events/donation/volunteer/{volunteerId}/history`

**Description:** Get donation history for a specific volunteer across all donation events.

#### Path Parameters

| Parameter   | Type | Required | Description             |
| ----------- | ---- | -------- | ----------------------- |
| volunteerId | Long | Yes      | The ID of the volunteer |

#### Response

**Success Response (200 OK):**

```json
[
  {
    "id": 1,
    "donationEventId": 1,
    "donationEventTitle": "Medical Equipment Fund",
    "donationEventDescription": "Fundraising for medical equipment for local hospitals",
    "moneyNeed": 100000000,
    "eventStatus": "ONGOING",
    "note": "Critical medical supplies needed",
    "qrPic": "qr_code_url",
    "bankAccount": "1234567890",
    "pic": "donation_image_url",
    "eventCreatedAt": "2024-01-20T10:00:00",
    "eventUpdatedAt": "2024-01-20T10:00:00",
    "organizationId": 1,
    "organizationName": "Green Earth Foundation",
    "organizationDescription": "Environmental protection organization",
    "donateAmount": 5000000,
    "donationNote": "Hope this helps",
    "donationCreatedAt": "2024-01-21T14:30:00",
    "donationUpdatedAt": "2024-01-21T14:30:00"
  },
  {
    "id": 2,
    "donationEventId": 2,
    "donationEventTitle": "Children's Education Fund",
    "donationEventDescription": "Supporting education for underprivileged children",
    "moneyNeed": 50000000,
    "eventStatus": "COMPLETED",
    "note": "Education materials and school supplies",
    "qrPic": "qr_code_url_2",
    "bankAccount": "0987654321",
    "pic": "education_image_url",
    "eventCreatedAt": "2024-01-15T09:00:00",
    "eventUpdatedAt": "2024-01-25T15:30:00",
    "organizationId": 2,
    "organizationName": "Children's Hope",
    "organizationDescription": "Supporting underprivileged children",
    "donateAmount": 3000000,
    "donationNote": "For the children",
    "donationCreatedAt": "2024-01-22T10:15:00",
    "donationUpdatedAt": "2024-01-22T10:15:00"
  }
]
```

#### Example Request

```
GET /api/events/donation/volunteer/1/history
```

---

## API Endpoints

### 1. Get All Donation Events

**Endpoint:** `GET /api/admin/donation-events`

**Description:** Get a list of all donation events with organization information and total donated amounts. Supports optional search functionality.

#### Query Parameters

| Parameter | Type   | Required | Description                                                              |
| --------- | ------ | -------- | ------------------------------------------------------------------------ |
| search    | String | No       | Search donation events by title or organization name (case-insensitive). |

#### Response

**Success Response (200 OK):**

```json
[
  {
    "id": 1,
    "title": "Medical Equipment Fund",
    "description": "Fundraising for medical equipment for local hospitals",
    "moneyNeed": 100000000,
    "eventStatus": "ONGOING",
    "hasDonate": true,
    "note": "Critical medical supplies needed",
    "qrPic": "qr_code_url",
    "bankAccount": "1234567890",
    "pic": "donation_image_url",
    "createdAt": "2024-01-20T10:00:00",
    "updatedAt": "2024-01-20T10:00:00",
    "organizationId": 1,
    "organizationName": "Green Earth Foundation",
    "organizationDescription": "Environmental protection organization",
    "totalDonatedAmount": 75000000
  },
  {
    "id": 2,
    "title": "Children's Education Fund",
    "description": "Supporting education for underprivileged children",
    "moneyNeed": 50000000,
    "eventStatus": "COMPLETED",
    "hasDonate": true,
    "note": "Education materials and school supplies",
    "qrPic": "qr_code_url_2",
    "bankAccount": "0987654321",
    "pic": "education_image_url",
    "createdAt": "2024-01-15T09:00:00",
    "updatedAt": "2024-01-25T15:30:00",
    "organizationId": 2,
    "organizationName": "Children's Hope",
    "organizationDescription": "Supporting underprivileged children",
    "totalDonatedAmount": 52000000
  }
]
```

#### Example Request

```
GET /api/admin/donation-events
```

**With Search:**

```
GET /api/admin/donation-events?search=medical
```

---

### 2. Get Donation Event Detail

**Endpoint:** `GET /api/admin/donation-events/{id}`

**Description:** Get detailed information about a specific donation event including donor list and available organizations.

#### Path Parameters

| Parameter | Type | Required | Description                  |
| --------- | ---- | -------- | ---------------------------- |
| id        | Long | Yes      | The ID of the donation event |

#### Response

**Success Response (200 OK):**

```json
{
  "id": 1,
  "title": "Medical Equipment Fund",
  "description": "Fundraising for medical equipment for local hospitals",
  "moneyNeed": 100000000,
  "eventStatus": "ONGOING",
  "hasDonate": true,
  "note": "Critical medical supplies needed",
  "qrPic": "qr_code_url",
  "bankAccount": "1234567890",
  "pic": "donation_image_url",
  "createdAt": "2024-01-20T10:00:00",
  "updatedAt": "2024-01-20T10:00:00",
  "organizationId": 1,
  "organizationName": "Green Earth Foundation",
  "organizationDescription": "Environmental protection organization",
  "totalDonatedAmount": 75000000,
  "donors": [
    {
      "id": 1,
      "volunteerId": 1,
      "volunteerFullName": "John Doe",
      "volunteerContact": "+84987654321",
      "volunteerEmail": "john.doe@example.com",
      "donateAmount": 5000000,
      "note": "Hope this helps",
      "createdAt": "2024-01-21T14:30:00",
      "updatedAt": "2024-01-21T14:30:00"
    },
    {
      "id": 2,
      "volunteerId": 2,
      "volunteerFullName": "Jane Smith",
      "volunteerContact": "+84444555666",
      "volunteerEmail": "jane.smith@example.com",
      "donateAmount": 3000000,
      "note": "For the children",
      "createdAt": "2024-01-22T10:15:00",
      "updatedAt": "2024-01-22T10:15:00"
    }
  ],
  "availableOrganizations": [
    {
      "id": 1,
      "organizationName": "Green Earth Foundation",
      "description": "Environmental protection organization"
    },
    {
      "id": 2,
      "organizationName": "Children's Hope",
      "description": "Supporting underprivileged children"
    }
  ]
}
```

#### Example Request

```
GET /api/admin/donation-events/1
```

---

### 3. Create Donation Event

**Endpoint:** `POST /api/admin/donation-events`

**Description:** Create a new donation event.

#### Request Body

```json
{
  "organizationId": 1,
  "title": "Emergency Relief Fund",
  "description": "Emergency relief for disaster victims",
  "moneyNeed": 200000000,
  "eventStatus": "UPCOMING",
  "hasDonate": false,
  "note": "Urgent help needed",
  "qrPic": "qr_code_url",
  "bankAccount": "1122334455",
  "pic": "relief_image_url"
}
```

#### Request Body Fields

| Field          | Type       | Required | Description                                           |
| -------------- | ---------- | -------- | ----------------------------------------------------- |
| organizationId | Long       | Yes      | ID of the organization organizing the event           |
| title          | String     | Yes      | Title of the donation event                           |
| description    | String     | No       | Description of the donation event                     |
| moneyNeed      | BigDecimal | Yes      | Target amount needed (must be > 0)                    |
| eventStatus    | String     | No       | Event status (default: UPCOMING)                      |
| hasDonate      | Boolean    | No       | Whether donations are being accepted (default: false) |
| note           | String     | No       | Additional notes                                      |
| qrPic          | String     | No       | QR code image URL                                     |
| bankAccount    | String     | No       | Bank account number for donations                     |
| pic            | String     | No       | Event image URL                                       |

#### Response

**Success Response (200 OK):**

```json
{
  "id": 3,
  "title": "Emergency Relief Fund",
  "description": "Emergency relief for disaster victims",
  "moneyNeed": 200000000,
  "eventStatus": "UPCOMING",
  "hasDonate": false,
  "note": "Urgent help needed",
  "qrPic": "qr_code_url",
  "bankAccount": "1122334455",
  "pic": "relief_image_url",
  "createdAt": "2024-01-25T16:00:00",
  "updatedAt": "2024-01-25T16:00:00",
  "organizationId": 1,
  "organizationName": "Green Earth Foundation",
  "organizationDescription": "Environmental protection organization",
  "totalDonatedAmount": 0
}
```

#### Example Request

```
POST /api/admin/donation-events
Content-Type: application/json

{
  "organizationId": 1,
  "title": "Emergency Relief Fund",
  "description": "Emergency relief for disaster victims",
  "moneyNeed": 200000000,
  "eventStatus": "UPCOMING",
  "hasDonate": false,
  "note": "Urgent help needed",
  "qrPic": "qr_code_url",
  "bankAccount": "1122334455",
  "pic": "relief_image_url"
}
```

---

### 4. Update Donation Event

**Endpoint:** `PUT /api/admin/donation-events/{id}`

**Description:** Update an existing donation event.

#### Path Parameters

| Parameter | Type | Required | Description                  |
| --------- | ---- | -------- | ---------------------------- |
| id        | Long | Yes      | The ID of the donation event |

#### Request Body

```json
{
  "title": "Updated Medical Equipment Fund",
  "description": "Updated description for medical equipment",
  "moneyNeed": 150000000,
  "eventStatus": "ONGOING",
  "hasDonate": true,
  "note": "Updated notes",
  "qrPic": "updated_qr_code_url",
  "bankAccount": "9876543210",
  "pic": "updated_image_url"
}
```

#### Request Body Fields

| Field          | Type       | Required | Description                                 |
| -------------- | ---------- | -------- | ------------------------------------------- |
| organizationId | Long       | No       | ID of the organization organizing the event |
| title          | String     | No       | Title of the donation event                 |
| description    | String     | No       | Description of the donation event           |
| moneyNeed      | BigDecimal | No       | Target amount needed (must be > 0)          |
| eventStatus    | String     | No       | Event status                                |
| hasDonate      | Boolean    | No       | Whether donations are being accepted        |
| note           | String     | No       | Additional notes                            |
| qrPic          | String     | No       | QR code image URL                           |
| bankAccount    | String     | No       | Bank account number for donations           |
| pic            | String     | No       | Event image URL                             |

#### Response

**Success Response (200 OK):**

```json
{
  "id": 1,
  "title": "Updated Medical Equipment Fund",
  "description": "Updated description for medical equipment",
  "moneyNeed": 150000000,
  "eventStatus": "ONGOING",
  "hasDonate": true,
  "note": "Updated notes",
  "qrPic": "updated_qr_code_url",
  "bankAccount": "9876543210",
  "pic": "updated_image_url",
  "createdAt": "2024-01-20T10:00:00",
  "updatedAt": "2024-01-25T17:00:00",
  "organizationId": 1,
  "organizationName": "Green Earth Foundation",
  "organizationDescription": "Environmental protection organization",
  "totalDonatedAmount": 75000000
}
```

#### Example Request

```
PUT /api/admin/donation-events/1
Content-Type: application/json

{
  "title": "Updated Medical Equipment Fund",
  "description": "Updated description for medical equipment",
  "moneyNeed": 150000000,
  "eventStatus": "ONGOING",
  "hasDonate": true,
  "note": "Updated notes",
  "qrPic": "updated_qr_code_url",
  "bankAccount": "9876543210",
  "pic": "updated_image_url"
}
```

---

### 5. Soft Delete Donation Event

**Endpoint:** `DELETE /api/admin/donation-events/{id}`

**Description:** Soft delete a donation event (marks as deleted and sets deleted_at timestamp).

#### Path Parameters

| Parameter | Type | Required | Description                  |
| --------- | ---- | -------- | ---------------------------- |
| id        | Long | Yes      | The ID of the donation event |

#### Response

**Success Response (200 OK):**

```json
{
  "message": "Donation event deleted successfully"
}
```

#### Example Request

```
DELETE /api/admin/donation-events/1
```

---

### 6. Update Donation Event Status

**Endpoint:** `PUT /api/admin/donation-events/{id}/status`

**Description:** Update the status of a donation event (e.g., stop event, complete event).

#### Path Parameters

| Parameter | Type | Required | Description                  |
| --------- | ---- | -------- | ---------------------------- |
| id        | Long | Yes      | The ID of the donation event |

#### Request Body

```json
{
  "eventStatus": "COMPLETED"
}
```

#### Request Body Fields

| Field       | Type   | Required | Description                                               |
| ----------- | ------ | -------- | --------------------------------------------------------- |
| eventStatus | String | Yes      | New event status: UPCOMING, ONGOING, COMPLETED, CANCELLED |

#### Response

**Success Response (200 OK):**

```json
{
  "id": 1,
  "title": "Medical Equipment Fund",
  "description": "Fundraising for medical equipment for local hospitals",
  "moneyNeed": 100000000,
  "eventStatus": "COMPLETED",
  "hasDonate": true,
  "note": "Critical medical supplies needed",
  "qrPic": "qr_code_url",
  "bankAccount": "1234567890",
  "pic": "donation_image_url",
  "createdAt": "2024-01-20T10:00:00",
  "updatedAt": "2024-01-25T18:00:00",
  "organizationId": 1,
  "organizationName": "Green Earth Foundation",
  "organizationDescription": "Environmental protection organization",
  "totalDonatedAmount": 75000000
}
```

#### Example Request

```
PUT /api/admin/donation-events/1/status
Content-Type: application/json

{
  "eventStatus": "COMPLETED"
}
```

---

## Business Logic Notes

### Donation Event Management

- **Total Donated Amount**: Calculated by summing all donations from volunteers for the specific event
- **Organization Information**: Includes organization details for each donation event
- **Donor List**: Complete list of volunteers who have donated to the event with amounts and timestamps
- **Available Organizations**: List of organizations with approved requests that can be assigned to events

### Volunteer Donation History

- **Donation History**: Complete list of all donations made by a specific volunteer across all donation events
- **Event Information**: Full details of each donation event including title, description, target amount, and status
- **Organization Information**: Details of the organization organizing each donation event
- **Donation Details**: Amount donated, donation note, and timestamps for each donation
- **Access Control**: Available to both admin and regular users (no authentication required for this endpoint)

### Search Functionality

- **Search Parameter**: Optional `search` query parameter for filtering donation events
- **Search Fields**: Searches in both donation event title and organization name
- **Case Insensitive**: Search is case-insensitive for better user experience
- **Partial Matching**: Uses "contains" matching for flexible search results

### Event Status Management

- **UPCOMING**: Event is planned but not yet started
- **ONGOING**: Event is currently active and accepting donations
- **COMPLETED**: Event has finished successfully
- **CANCELLED**: Event has been cancelled

### Soft Delete

- When deleting a donation event, both `is_deleted` is set to true and `deleted_at` is set to current timestamp
- Soft deleted events are not removed from database but are excluded from normal queries

### Data Relationships

- Each donation event is linked to an organization
- Each donation event can have multiple volunteer donations
- Organization must have an approved request to be available for event assignment

---

## Error Responses

### 400 Bad Request

```json
{
  "message": "Money need must be greater than 0"
}
```

### 404 Not Found

```json
{
  "message": "Donation event not found with id: 1"
}
```

### 500 Internal Server Error

```json
{
  "message": "Internal server error occurred"
}
```

---

## Event Status Values

- **UPCOMING**: Event is planned but not yet started
- **ONGOING**: Event is currently active and accepting donations
- **COMPLETED**: Event has finished successfully
- **CANCELLED**: Event has been cancelled

---

## Notes

- **List API**: Returns all donation events with organization info and total donated amounts
- **Search Functionality**: Optional search by title or organization name (case-insensitive)
- **Detail API**: Provides comprehensive information including donor list and available organizations
- **Volunteer History API**: Get complete donation history for a specific volunteer across all events
- **Create/Update**: Full CRUD operations with validation
- **Soft Delete**: Marks events as deleted without removing from database
- **Status Update**: Allows changing event status (e.g., stopping events)
- **No Pagination**: All list APIs return complete datasets without pagination
- **Real-time Totals**: Donated amounts are calculated in real-time from actual donations
- **Universal Access**: Volunteer donation history API is accessible to both admin and regular users
