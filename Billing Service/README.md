# Billing Service

## Overview

Billing Service manages billing accounts for patients. It exposes a gRPC endpoint that Patient Service calls when a new
patient is created. This service is not directly accessible from outside, it is only called internally by Patient
Service

## Ports

| Port | Protocol |
|------|----------|
| 4001 | HTTP     |
| 9001 | gRPC     |

## gRPC Service

The service implements `BillingServiceImplBase` and provides:

### CreateBillingAccount

Creates a billing account for a patient

**Request:**

1. patientId
2. name
3. email

**Response:**

1. accountId
2. status (ACTIVE)

## Proto File

The service definition is in [billing_service.proto](src/main/proto/billing_service.proto)

## Tech

1. Spring Boot
2. gRPC (grpc-server-spring-boot-starter)
3. Protocol Buffers

## How It Works

1. Patient Service creates a new patient
2. Patient Service calls Billing Service via gRPC
3. Billing Service creates a billing account
4. Returns account details to Patient Service

## Running Locally

```bash
./mvnw spring-boot:run
```

The gRPC server will start on port 9001

## Testing gRPC

You can use tools like grpcurl or BloomRPC to test the gRPC endpoints. Sample requests are in
the [grpc-requests](../grpc-requests) folder in the root directory

[Back to Main README](../README.md)
