# API Gateway

## Overview

The API Gateway is the entry point for all client requests. It handles routing and JWT validation before forwarding
requests to the appropriate microservice

## Port

4004

## Features

1. Routes `/auth/**` requests to Auth Service
2. Routes `/api/patients/**` requests to Patient Service
3. Validates JWT tokens by calling Auth Service before routing protected requests
4. Provides unified API documentation endpoints

## Routes

| Path               | Target Service          | Auth Required |
|--------------------|-------------------------|---------------|
| /auth/**           | Auth Service (4005)     | No            |
| /api/patients/**   | Patient Service (4000)  | Yes           |
| /api-docs/auth     | Auth Service Swagger    | No            |
| /api-docs/patients | Patient Service Swagger | No            |

## How JWT Validation Works

The gateway uses a
custom [JwtValidationGatewayFilterFactory.java](src/main/java/com/sathwikhbhat/apigateway/filter/JwtValidationGatewayFilterFactory.java)
that:

1. Extracts the Bearer token from the Authorization header
2. Calls Auth Service `/validate` endpoint
3. If valid, forwards the request
4. If invalid, returns 401 Unauthorized

## Tech

1. Spring Cloud Gateway
2. Spring WebFlux (reactive)
3. WebClient for Auth Service calls

## Configuration

See [application.yml](src/main/resources/application.yml) for route configurations

## Running Locally

```bash
./mvnw spring-boot:run
```

Or with Docker:

```bash
docker build -t api-gateway .
docker run -p 4004:4004 api-gateway
```

[Back to Main README](../README.md)
