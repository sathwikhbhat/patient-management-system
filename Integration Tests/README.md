# Integration Tests

## Overview

End to end integration tests for the Patient Management System. Tests the complete flow from API Gateway through all
services

## Tech

1. JUnit 5
2. REST Assured

## Test Flow

The main test ([PatientIntegrationTest.java](src/test/java/PatientIntegrationTest.java)) does the following:

1. Sends login request to `/auth/login`
2. Extracts JWT token from response
3. Uses the token to call `/api/patients`
4. Verifies the response

## Running Tests

First, make sure all services are running (use docker compose from root)

Then run:

```bash
./mvnw test
```

Or run specific test:

```bash
./mvnw test -Dtest=PatientIntegrationTest
```

## Test Configuration

Tests connect to API Gateway at `http://localhost:4004`

## Adding More Tests

Add new test classes in [src/test/java](src/test/java). Use REST Assured for HTTP calls and JUnit 5 for assertions

Example:

```java

@Test
void shouldCreatePatient() {
    String token = getToken();

    given()
            .header("Authorization", "Bearer " + token)
            .contentType("application/json")
            .body(patientJson)
            .when()
            .post("/api/patients")
            .then()
            .statusCode(200);
}
```

[Back to Main README](../README.md)
