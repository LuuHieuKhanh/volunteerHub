# Charity Event Management API Documentation

## Overview

This document describes the Charity Event Management APIs for the Volunteer Hub system. These APIs allow administrators to manage charity events including creating, updating, deleting, and monitoring volunteer participation activities.

## Base URL

```
http://localhost:8080/api/admin/charity-events
```

---

## API Endpoints

### 0. Get Volunteer Charity Event History

**Endpoint:** `GET /api/events/charity/volunteer/{volunteerId}/history`

**Description:** Get charity event participation history for a specific volunteer across all charity events.

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
    "charityEventId": 1,
    "charityEventName": "Community Cleanup",
    "charityEventDescription": "Cleaning up the local community park",
    "todo": "Collect trash, plant trees, paint benches",
    "requirement": "Bring gloves and water bottle",
    "destination": "Central Park",
    "dateStart": "2024-02-01T08:00:00",
    "dateEnd": "2024-02-01T17:00:00",
    "numVolunteerRequire": 50,
    "numVolunteerActual": 45,
    "note": "Weather dependent event",
    "pic": "cleanup_image_url",
    "eventStatus": "COMPLETED",
    "eventCreatedAt": "2024-01-20T10:00:00",
    "eventUpdatedAt": "2024-02-01T18:00:00",
    "organizationId": 1,
    "organizationName": "Green Earth Foundation",
    "organizationDescription": "Environmental protection organization",
    "joinStatus": "REGISTERED",
    "participationCreatedAt": "2024-01-21T14:30:00",
    "participationUpdatedAt": "2024-01-21T14:30:00"
  },
  {
    "id": 2,
    "charityEventId": 2,
    "charityEventName": "Food Distribution",
    "charityEventDescription": "Distributing food to homeless people",
    "todo": "Pack food, distribute to locations",
    "requirement": "Valid ID required",
    "destination": "Downtown Shelter",
    "dateStart": "2024-02-15T09:00:00",
    "dateEnd": "2024-02-15T15:00:00",
    "numVolunteerRequire": 30,
    "numVolunteerActual": 28,
    "note": "Bring warm clothes",
    "pic": "food_distribution_image_url",
    "eventStatus": "ONGOING",
    "eventCreatedAt": "2024-01-25T09:00:00",
    "eventUpdatedAt": "2024-02-15T10:00:00",
    "organizationId": 2,
    "organizationName": "Helping Hands",
    "organizationDescription": "Supporting homeless community",
    "joinStatus": "REGISTERED",
    "participationCreatedAt": "2024-01-26T10:15:00",
    "participationUpdatedAt": "2024-01-26T10:15:00"
  }
]
```

#### Example Request

```
GET /api/events/charity/volunteer/1/history
```

---

## API Endpoints

### 1. Get All Charity Events

**Endpoint:** `GET /api/admin/charity-events`

**Description:** Get a list of all charity events with organization information and total participant counts. Supports optional search functionality.

#### Query Parameters

| Parameter | Type   | Required | Description                                                            |
| --------- | ------ | -------- | ---------------------------------------------------------------------- |
| search    | String | No       | Search charity events by name or organization name (case-insensitive). |

#### Response

**Success Response (200 OK):**

```json
[
  {
    "id": 1,
    "charityName": "Community Cleanup",
    "description": "Cleaning up the local community park",
    "todo": "Collect trash, plant trees, paint benches",
    "requirement": "Bring gloves and water bottle",
    "destination": "Central Park",
    "dateStart": "2024-02-01T08:00:00",
    "dateEnd": "2024-02-01T17:00:00",
    "numVolunteerRequire": 50,
    "numVolunteerActual": 45,
    "note": "Weather dependent event",
    "pic": "cleanup_image_url",
    "eventStatus": "COMPLETED",
    "createdAt": "2024-01-20T10:00:00",
    "updatedAt": "2024-02-01T18:00:00",
    "organizationId": 1,
    "organizationName": "Green Earth Foundation",
    "organizationDescription": "Environmental protection organization",
    "totalParticipants": 45
  },
  {
    "id": 2,
    "charityName": "Food Distribution",
    "description": "Distributing food to homeless people",
    "todo": "Pack food, distribute to locations",
    "requirement": "Valid ID required",
    "destination": "Downtown Shelter",
    "dateStart": "2024-02-15T09:00:00",
    "dateEnd": "2024-02-15T15:00:00",
    "numVolunteerRequire": 30,
    "numVolunteerActual": 28,
    "note": "Bring warm clothes",
    "pic": "food_distribution_image_url",
    "eventStatus": "ONGOING",
    "createdAt": "2024-01-25T09:00:00",
    "updatedAt": "2024-02-15T10:00:00",
    "organizationId": 2,
    "organizationName": "Helping Hands",
    "organizationDescription": "Supporting homeless community",
    "totalParticipants": 28
  }
]
```

#### Example Request

```
GET /api/admin/charity-events
```

**With Search:**

```
GET /api/admin/charity-events?search=cleanup
```

---

### 2. Get Charity Event Detail

**Endpoint:** `GET /api/admin/charity-events/{id}`

**Description:** Get detailed information about a specific charity event including participant list and available organizations.

#### Path Parameters

| Parameter | Type | Required | Description                 |
| --------- | ---- | -------- | --------------------------- |
| id        | Long | Yes      | The ID of the charity event |

#### Response

**Success Response (200 OK):**

```json
{
  "id": 1,
  "charityName": "Community Cleanup",
  "description": "Cleaning up the local community park",
  "todo": "Collect trash, plant trees, paint benches",
  "requirement": "Bring gloves and water bottle",
  "destination": "Central Park",
  "dateStart": "2024-02-01T08:00:00",
  "dateEnd": "2024-02-01T17:00:00",
  "numVolunteerRequire": 50,
  "numVolunteerActual": 45,
  "note": "Weather dependent event",
  "pic": "cleanup_image_url",
  "eventStatus": "COMPLETED",
  "createdAt": "2024-01-20T10:00:00",
  "updatedAt": "2024-02-01T18:00:00",
  "organizationId": 1,
  "organizationName": "Green Earth Foundation",
  "organizationDescription": "Environmental protection organization",
  "totalParticipants": 45,
  "participants": [
    {
      "id": 1,
      "volunteerId": 1,
      "volunteerFullName": "John Doe",
      "volunteerContact": "+84987654321",
      "volunteerEmail": "john.doe@example.com",
      "joinStatus": "REGISTERED",
      "createdAt": "2024-01-21T14:30:00",
      "updatedAt": "2024-01-21T14:30:00"
    },
    {
      "id": 2,
      "volunteerId": 2,
      "volunteerFullName": "Jane Smith",
      "volunteerContact": "+84444555666",
      "volunteerEmail": "jane.smith@example.com",
      "joinStatus": "REGISTERED",
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
      "organizationName": "Helping Hands",
      "description": "Supporting homeless community"
    }
  ]
}
```

#### Example Request

```
GET /api/admin/charity-events/1
```

---

### 3. Create Charity Event

**Endpoint:** `POST /api/admin/charity-events`

**Description:** Create a new charity event.

#### Request Body

```json
{
  "organizationId": 1,
  "charityName": "Beach Cleanup",
  "description": "Cleaning up the local beach area",
  "todo": "Collect plastic waste, organize recycling",
  "requirement": "Bring sunscreen and water",
  "destination": "Sunset Beach",
  "dateStart": "2024-03-01T08:00:00",
  "dateEnd": "2024-03-01T16:00:00",
  "numVolunteerRequire": 40,
  "note": "High tide at 2 PM",
  "pic": "beach_cleanup_image_url",
  "eventStatus": "UPCOMING"
}
```

#### Request Body Fields

| Field               | Type          | Required | Description                                 |
| ------------------- | ------------- | -------- | ------------------------------------------- |
| organizationId      | Long          | Yes      | ID of the organization organizing the event |
| charityName         | String        | Yes      | Name of the charity event                   |
| description         | String        | No       | Description of the charity event            |
| todo                | String        | No       | List of tasks to be performed               |
| requirement         | String        | No       | Requirements for volunteers                 |
| destination         | String        | No       | Location where the event takes place        |
| dateStart           | LocalDateTime | Yes      | Start date and time of the event            |
| dateEnd             | LocalDateTime | Yes      | End date and time of the event              |
| numVolunteerRequire | Long          | Yes      | Number of volunteers required (must be ≥ 1) |
| note                | String        | No       | Additional notes                            |
| pic                 | String        | No       | Event image URL                             |
| eventStatus         | String        | No       | Event status (default: UPCOMING)            |

#### Response

**Success Response (200 OK):**

```json
{
  "id": 3,
  "charityName": "Beach Cleanup",
  "description": "Cleaning up the local beach area",
  "todo": "Collect plastic waste, organize recycling",
  "requirement": "Bring sunscreen and water",
  "destination": "Sunset Beach",
  "dateStart": "2024-03-01T08:00:00",
  "dateEnd": "2024-03-01T16:00:00",
  "numVolunteerRequire": 40,
  "numVolunteerActual": 0,
  "note": "High tide at 2 PM",
  "pic": "beach_cleanup_image_url",
  "eventStatus": "UPCOMING",
  "createdAt": "2024-01-25T16:00:00",
  "updatedAt": "2024-01-25T16:00:00",
  "organizationId": 1,
  "organizationName": "Green Earth Foundation",
  "organizationDescription": "Environmental protection organization",
  "totalParticipants": 0
}
```

#### Example Request

```
POST /api/admin/charity-events
Content-Type: application/json

{
  "organizationId": 1,
  "charityName": "Beach Cleanup",
  "description": "Cleaning up the local beach area",
  "todo": "Collect plastic waste, organize recycling",
  "requirement": "Bring sunscreen and water",
  "destination": "Sunset Beach",
  "dateStart": "2024-03-01T08:00:00",
  "dateEnd": "2024-03-01T16:00:00",
  "numVolunteerRequire": 40,
  "note": "High tide at 2 PM",
  "pic": "beach_cleanup_image_url",
  "eventStatus": "UPCOMING"
}
```

---

### 4. Update Charity Event

**Endpoint:** `PUT /api/admin/charity-events/{id}`

**Description:** Update an existing charity event.

#### Path Parameters

| Parameter | Type | Required | Description                 |
| --------- | ---- | -------- | --------------------------- |
| id        | Long | Yes      | The ID of the charity event |

#### Request Body

```json
{
  "charityName": "Updated Beach Cleanup",
  "description": "Updated description for beach cleanup",
  "todo": "Updated tasks: collect waste, plant mangroves",
  "requirement": "Updated requirements",
  "destination": "Updated Beach Location",
  "dateStart": "2024-03-02T08:00:00",
  "dateEnd": "2024-03-02T16:00:00",
  "numVolunteerRequire": 50,
  "note": "Updated notes",
  "pic": "updated_beach_cleanup_image_url",
  "eventStatus": "ONGOING"
}
```

#### Request Body Fields

| Field               | Type          | Required | Description                                 |
| ------------------- | ------------- | -------- | ------------------------------------------- |
| organizationId      | Long          | No       | ID of the organization organizing the event |
| charityName         | String        | No       | Name of the charity event                   |
| description         | String        | No       | Description of the charity event            |
| todo                | String        | No       | List of tasks to be performed               |
| requirement         | String        | No       | Requirements for volunteers                 |
| destination         | String        | No       | Location where the event takes place        |
| dateStart           | LocalDateTime | No       | Start date and time of the event            |
| dateEnd             | LocalDateTime | No       | End date and time of the event              |
| numVolunteerRequire | Long          | No       | Number of volunteers required (must be ≥ 1) |
| note                | String        | No       | Additional notes                            |
| pic                 | String        | No       | Event image URL                             |
| eventStatus         | String        | No       | Event status                                |

#### Response

**Success Response (200 OK):**

```json
{
  "id": 1,
  "charityName": "Updated Beach Cleanup",
  "description": "Updated description for beach cleanup",
  "todo": "Updated tasks: collect waste, plant mangroves",
  "requirement": "Updated requirements",
  "destination": "Updated Beach Location",
  "dateStart": "2024-03-02T08:00:00",
  "dateEnd": "2024-03-02T16:00:00",
  "numVolunteerRequire": 50,
  "numVolunteerActual": 45,
  "note": "Updated notes",
  "pic": "updated_beach_cleanup_image_url",
  "eventStatus": "ONGOING",
  "createdAt": "2024-01-20T10:00:00",
  "updatedAt": "2024-01-25T17:00:00",
  "organizationId": 1,
  "organizationName": "Green Earth Foundation",
  "organizationDescription": "Environmental protection organization",
  "totalParticipants": 45
}
```

#### Example Request

```
PUT /api/admin/charity-events/1
Content-Type: application/json

{
  "charityName": "Updated Beach Cleanup",
  "description": "Updated description for beach cleanup",
  "todo": "Updated tasks: collect waste, plant mangroves",
  "requirement": "Updated requirements",
  "destination": "Updated Beach Location",
  "dateStart": "2024-03-02T08:00:00",
  "dateEnd": "2024-03-02T16:00:00",
  "numVolunteerRequire": 50,
  "note": "Updated notes",
  "pic": "updated_beach_cleanup_image_url",
  "eventStatus": "ONGOING"
}
```

---

### 5. Soft Delete Charity Event

**Endpoint:** `DELETE /api/admin/charity-events/{id}`

**Description:** Soft delete a charity event (marks as deleted and sets deleted_at timestamp).

#### Path Parameters

| Parameter | Type | Required | Description                 |
| --------- | ---- | -------- | --------------------------- |
| id        | Long | Yes      | The ID of the charity event |

#### Response

**Success Response (200 OK):**

```json
{
  "message": "Charity event deleted successfully"
}
```

#### Example Request

```
DELETE /api/admin/charity-events/1
```

---

### 6. Update Charity Event Status

**Endpoint:** `PUT /api/admin/charity-events/{id}/status`

**Description:** Update the status of a charity event (e.g., start event, complete event).

#### Path Parameters

| Parameter | Type | Required | Description                 |
| --------- | ---- | -------- | --------------------------- |
| id        | Long | Yes      | The ID of the charity event |

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
  "charityName": "Community Cleanup",
  "description": "Cleaning up the local community park",
  "todo": "Collect trash, plant trees, paint benches",
  "requirement": "Bring gloves and water bottle",
  "destination": "Central Park",
  "dateStart": "2024-02-01T08:00:00",
  "dateEnd": "2024-02-01T17:00:00",
  "numVolunteerRequire": 50,
  "numVolunteerActual": 45,
  "note": "Weather dependent event",
  "pic": "cleanup_image_url",
  "eventStatus": "COMPLETED",
  "createdAt": "2024-01-20T10:00:00",
  "updatedAt": "2024-01-25T18:00:00",
  "organizationId": 1,
  "organizationName": "Green Earth Foundation",
  "organizationDescription": "Environmental protection organization",
  "totalParticipants": 45
}
```

#### Example Request

```
PUT /api/admin/charity-events/1/status
Content-Type: application/json

{
  "eventStatus": "COMPLETED"
}
```

---

## Business Logic Notes

### Charity Event Management

- **Total Participants Count**: Calculated by counting all volunteer participations for the specific event
- **Organization Information**: Includes organization details for each charity event
- **Participant List**: Complete list of volunteers who have joined the event with join status and timestamps
- **Available Organizations**: List of organizations with approved requests that can be assigned to events

### Volunteer Charity Event History

- **Participation History**: Complete list of all charity event participations by a specific volunteer across all events
- **Event Information**: Full details of each charity event including name, description, tasks, requirements, and status
- **Organization Information**: Details of the organization organizing each charity event
- **Participation Details**: Join status, participation timestamps, and event details for each participation
- **Access Control**: Available to both admin and regular users (no authentication required for this endpoint)

### Search Functionality

- **Search Parameter**: Optional `search` query parameter for filtering charity events
- **Search Fields**: Searches in both charity event name and organization name
- **Case Insensitive**: Search is case-insensitive for better user experience
- **Partial Matching**: Uses "contains" matching for flexible search results

### Event Status Management

- **UPCOMING**: Event is planned but not yet started
- **ONGOING**: Event is currently active and accepting volunteers
- **COMPLETED**: Event has finished successfully
- **CANCELLED**: Event has been cancelled

### Soft Delete

- When deleting a charity event, both `is_deleted` is set to true and `deleted_at` is set to current timestamp
- Soft deleted events are not removed from database but are excluded from normal queries

### Data Relationships

- Each charity event is linked to an organization
- Each charity event can have multiple volunteer participations
- Organization must have an approved request to be available for event assignment

---

## Error Responses

### 400 Bad Request

```json
{
  "message": "Number of volunteers required must be at least 1"
}
```

### 404 Not Found

```json
{
  "message": "Charity event not found with id: 1"
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
- **ONGOING**: Event is currently active and accepting volunteers
- **COMPLETED**: Event has finished successfully
- **CANCELLED**: Event has been cancelled

---

## Join Status Values

- **REGISTERED**: Volunteer has registered for the event
- **CONFIRMED**: Volunteer's participation has been confirmed
- **CANCELLED**: Volunteer has cancelled their participation

---

## Notes

- **List API**: Returns all charity events with organization info and total participant counts
- **Search Functionality**: Optional search by name or organization name (case-insensitive)
- **Detail API**: Provides comprehensive information including participant list and available organizations
- **Volunteer History API**: Get complete participation history for a specific volunteer across all events
- **Create/Update**: Full CRUD operations with validation
- **Soft Delete**: Marks events as deleted without removing from database
- **Status Update**: Allows changing event status (e.g., starting events, completing events)
- **No Pagination**: All list APIs return complete datasets without pagination
- **Real-time Counts**: Participant counts are calculated in real-time from actual participations
- **Universal Access**: Volunteer charity event history API is accessible to both admin and regular users
