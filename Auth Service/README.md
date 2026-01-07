# Auth Service

## Overview

Auth Service handles user authentication and JWT token management. It validates credentials, generates tokens, and
provides token validation for other services

## Port

4005 (internal), accessed via API Gateway at 4004

## Endpoints (via API Gateway)

| Method | Path           | Description                            |
|--------|----------------|----------------------------------------|
| POST   | /auth/login    | Authenticate user and return JWT token |
| GET    | /auth/validate | Validate a JWT token                   |

## How It Works

1. User sends email and password to `/auth/login` via API Gateway
2. Service checks credentials against the database
3. If valid, generates a JWT token with 12 hour expiry
4. Token includes user email and role as claims
5. API Gateway calls `/validate` internally to verify tokens on protected routes

## Tech

1. Spring Boot
2. Spring Security
3. JWT (io.jsonwebtoken)
4. PostgreSQL for user storage
5. BCrypt for password hashing

## Database

Uses PostgreSQL with a User table. Initial data is loaded from [data.sql](src/main/resources/data.sql)

## JWT Details

1. Algorithm: HMAC SHA
2. Expiry: 12 hours
3. Claims: subject (email), role, issuedAt, expiration

## Running Locally

```bash
./mvnw spring-boot:run
```

Make sure PostgreSQL is running and configured

## Sample Login Request

```json
{
  "email": "testuser@test.com",
  "password": "password123"
}
```

Response:

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

[Back to Main README](../README.md)
