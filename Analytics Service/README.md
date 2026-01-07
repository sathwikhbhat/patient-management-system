# Analytics Service

## Overview

Analytics Service consumes patient events from Kafka for analytics and reporting purposes. It listens to the `patient`
topic and processes events asynchronously. This service has no HTTP endpoints, it only consumes Kafka messages

## Port

No HTTP port exposed

## How It Works

1. Patient Service publishes events to Kafka `patient` topic
2. Analytics Service listens to the topic with group id `analytics-service`
3. Events are deserialized from Protocol Buffer format
4. Service logs and processes the events (business logic can be added here)

## Kafka Configuration

| Property           | Value                 |
|--------------------|-----------------------|
| Topic              | patient               |
| Group ID           | analytics-service     |
| Key Deserializer   | StringDeserializer    |
| Value Deserializer | ByteArrayDeserializer |

## Event Format

Events are in Protocol Buffer format (`PatientEvent`):

1. patientId
2. name
3. email
4. eventType (PATIENT_CREATED)

## Proto File

The event definition is in [patient_event.proto](src/main/proto/patient_event.proto)

## Tech

1. Spring Boot
2. Spring Kafka
3. Protocol Buffers

## Running Locally

```bash
./mvnw spring-boot:run
```

Make sure Kafka is running on localhost:9092

## Extending

Add your analytics business logic
in [KafkaConsumer.java](src/main/java/com/sathwikhbhat/analyticsservice/kafka/KafkaConsumer.java).
Currently, it just logs the events, but you can add:

1. Store events in a database
2. Calculate metrics
3. Send notifications
4. Generate reports

[Back to Main README](../README.md)
