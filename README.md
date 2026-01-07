# Patient Management System

A microservices-based patient management system built with Spring Boot. This is a personal project to learn and
demonstrate how different services can communicate using REST APIs, gRPC, and Kafka in a distributed system

## What This Project Does

This system allows you to manage patient records through a set of microservices. When you create a patient, the system
automatically:

1. Stores the patient in a PostgreSQL database
2. Creates a billing account for them by calling Billing Service via gRPC (a fast internal communication protocol)
3. Publishes a `PATIENT_CREATED` event to Kafka so Analytics Service can process it asynchronously

All requests go through an API Gateway that handles JWT authentication. You first login to get a token, then include
that token in all later requests. The gateway validates your token before forwarding requests to internal services

## Project Structure

*Click on any service name to view its detailed README*

| Service                                              | Description                                            | Port                     |
|------------------------------------------------------|--------------------------------------------------------|--------------------------|
| [API Gateway](./API%20Gateway/README.md)             | Entry point, routes requests and validates JWT tokens  | 4004                     |
| [Auth Service](./Auth%20Service/README.md)           | Handles user login and JWT token generation/validation | 4005                     |
| [Patient Service](./Patient%20Service/README.md)     | Core service for patient CRUD operations               | 4000                     |
| [Billing Service](./Billing%20Service/README.md)     | Manages billing accounts via gRPC                      | 4001 (HTTP), 9001 (gRPC) |
| [Analytics Service](./Analytics%20Service/README.md) | Consumes patient events from Kafka                     | No HTTP port             |
| [Integration Tests](./Integration%20Tests/README.md) | End to end tests for the system                        | N/A                      |

## Tech Stack

| Category         | Technology                          |
|------------------|-------------------------------------|
| Framework        | Spring Boot 4, Spring Cloud Gateway |
| Database         | PostgreSQL                          |
| Messaging        | Apache Kafka                        |
| RPC              | gRPC with Protocol Buffers          |
| Auth             | JWT (JSON Web Tokens)               |
| Containerization | Docker, Docker Compose              |
| Build Tool       | Maven                               |
| Language         | Java 21                             |

## Architecture Overview

```
                    +---------------+
                    |  API Gateway  |
                    |    (4004)     |
                    +-------+-------+
                            |
            +---------------+---------------+
            |                               |
    +-------v-------+               +-------v-------+
    |  Auth Service |               | Patient Service|
    |    (4005)     |               |    (4000)      |
    +---------------+               +-------+-------+
                                            |
                            +---------------+---------------+
                            |                               |
                    +-------v-------+               +-------v-------+
                    |Billing Service|               |     Kafka     |
                    | (4001/9001)   |               +-------+-------+
                    +---------------+                       |
                                                    +-------v-------+
                                                    | Analytics Svc |
                                                    +---------------+
```

## How It Works

1. Client sends request to API Gateway (port 4004). This is the only port exposed to the outside world
2. For auth endpoints (`/auth/*`), Gateway forwards to Auth Service directly since no authentication is needed to login
3. For patient endpoints (`/api/patients/*`), Gateway first extracts the JWT token from the Authorization header and
   calls Auth Service to validate it
4. If token is valid, the request is forwarded to Patient Service. If invalid or missing, Gateway returns 401
   Unauthorized
5. When creating a patient, Patient Service first saves to database, then calls Billing Service via gRPC to create a
   billing account. gRPC is used here because its faster than REST for service to service communication
6. After successful creation, Patient Service publishes a `PATIENT_CREATED` event to Kafka. This is asynchronous, so the
   API responds immediately without waiting
7. Analytics Service, which is subscribed to the Kafka topic, picks up the event and processes it in the background

## Getting Started

### Prerequisites

1. Docker and Docker Compose installed
2. Java 21 (if running locally without Docker)
3. Maven (if running locally without Docker)

### Running with Docker Compose

```bash
docker compose up
```

This will spin up all services including Kafka and PostgreSQL databases. Wait for about 30 seconds until you see logs
from all services. The API Gateway will be available at `http://localhost:4004`

### Default Test User

The Auth Service comes with a pre configured test user you can use right away:

| Email             | Password    | Role  |
|-------------------|-------------|-------|
| testuser@test.com | password123 | ADMIN |

### Running Locally (Without Docker)

You'll need to:

1. Start PostgreSQL (on port 5432) and Kafka (on port 9092) manually
2. Update the application properties in each service to point to localhost instead of the Docker service names
3. Run each service in order: Auth Service and Billing Service first, then Patient Service, then Analytics Service, and
   finally API Gateway
4. Start each service with `./mvnw spring-boot:run` from within its directory

## API Endpoints

All endpoints are accessed through the API Gateway at `http://localhost:4004`

### Authentication

| Method | Endpoint       | Description                                      | Auth Required      |
|--------|----------------|--------------------------------------------------|--------------------|
| POST   | /auth/login    | Login with email and password, returns JWT token | No                 |
| GET    | /auth/validate | Validate a JWT token                             | Yes (Bearer token) |

### Patient Management

| Method | Endpoint           | Description            | Auth Required |
|--------|--------------------|------------------------|---------------|
| GET    | /api/patients      | Get all patients       | Yes           |
| POST   | /api/patients      | Create a new patient   | Yes           |
| PUT    | /api/patients/{id} | Update a patient by ID | Yes           |
| DELETE | /api/patients/{id} | Delete a patient by ID | Yes           |

### API Documentation

| Endpoint           | Description                  |
|--------------------|------------------------------|
| /api-docs/auth     | Auth Service OpenAPI docs    |
| /api-docs/patients | Patient Service OpenAPI docs |

## Sample Requests

### Login

First, get a JWT token by logging in with the test user credentials

```bash
curl -X POST http://localhost:4004/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "testuser@test.com", "password": "password123"}'
```

This returns a token that expires in 12 hours. Copy the token value for the next requests

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### Get All Patients

Replace `<your-token>` with the token you received from login

```bash
curl http://localhost:4004/api/patients \
  -H "Authorization: Bearer <your-token>"
```

Returns an array of all patients in the database

### Create Patient

All fields are required. The `dateOfBirth` and `registeredDate` must be in YYYY-MM-DD format

```bash
curl -X POST http://localhost:4004/api/patients \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <your-token>" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "address": "123 Main St",
    "dateOfBirth": "1990-01-15",
    "registeredDate": "2026-01-01"
  }'
```

This creates the patient, triggers a gRPC call to Billing Service, and publishes an event to Kafka. The email must be
unique across all patients

More sample requests are available in the [api-requests](./api-requests) and [grpc-requests](./grpc-requests) folders
