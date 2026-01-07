# Patient Service

## Overview

The core service that handles all patient related operations. It manages patient data, communicates with Billing Service via gRPC, and publishes events to Kafka

## Port

4000 (internal), accessed via API Gateway at 4004

## Endpoints (via API Gateway)

All endpoints require JWT authentication

| Method | Path | Description |
|--------|------|-------------|
| GET | /api/patients | Get all patients |
| POST | /api/patients | Create a new patient |
| PUT | /api/patients/{id} | Update a patient |
| DELETE | /api/patients/{id} | Delete a patient |

## Patient Model

| Field | Type | Description |
|-------|------|-------------|
| id | UUID | Auto generated |
| name | String | Required, max 100 chars |
| email | String | Required, unique |
| address | String | Required |
| dateOfBirth | LocalDate | Required |
| registeredDate | LocalDate | Required on create |

## Features

1. CRUD operations for patients
2. Email uniqueness validation
3. Input validation with custom messages
4. gRPC call to Billing Service when creating a patient
5. Kafka event publishing for patient creation

## Service Integrations

### Billing Service (gRPC)

When a patient is created, the service calls Billing Service via gRPC to create a billing account

### Kafka

Publishes `PatientEvent` protobuf messages to the `patient` topic when a new patient is created. The event includes patientId, name, email, and eventType

## Tech

1. Spring Boot
2. Spring Data JPA
3. PostgreSQL
4. gRPC client
5. Kafka producer
6. Protocol Buffers
7. Swagger/OpenAPI

## Running Locally

```bash
./mvnw spring-boot:run
```

Needs PostgreSQL, Kafka, and Billing Service to be running for full functionality

## Sample Request

```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "address": "123 Main St",
  "dateOfBirth": "1990-01-15",
  "registeredDate": "2026-01-01"
}
```

[Back to Main README](../README.md)
