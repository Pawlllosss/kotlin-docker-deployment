# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Kotlin-based Spring Boot application designed for deployment on AWS ECS Fargate. The application integrates with AWS DynamoDB for data persistence and is containerized using Docker.

**Key Technologies:**
- Kotlin 2.2.0 with Spring Boot 4.0.0-M2
- Java 21 (Amazon Corretto)
- AWS SDK for DynamoDB (Enhanced Client)
- SpringDoc OpenAPI (Swagger)
- Docker with multi-platform support (amd64/arm64)

## Build and Run Commands

### Local Development
```bash
# Build the project
./gradlew build

# Run tests
./gradlew test

# Run the application locally
./gradlew bootRun

# Clean build
./gradlew clean build
```

### Docker
```bash
# Build JAR first
./gradlew build

# Build Docker image (local)
docker build -t kotlin-app .

# Run container locally
docker run -p 8080:8080 kotlin-app

# Multi-platform build (matching production)
docker buildx build --platform linux/amd64,linux/arm64 -t kotlin-app .
```

## Architecture

### AWS Integration
The application uses a multi-tiered AWS credential provider chain (DynamoDBConfig.kt:23-42):
1. Environment variables (for local development)
2. System properties
3. AWS profile credentials (~/.aws/credentials)
4. EC2/ECS instance profile
5. ECS container credentials
6. EKS web identity tokens

This allows the same codebase to work in local development (using AWS CLI credentials) and in production (using IAM roles).

### DynamoDB Configuration
- Region is configurable via `aws.dynamodb.region` property (defaults to us-east-1)
- Uses DynamoDB Enhanced Client with annotated POJOs
- Table schemas are defined using `@DynamoDbBean` and `@DynamoDbPartitionKey` annotations
- Example: `Prompt` entity uses UUID as partition key, stored in "prompts" table

### Package Structure
```
pl.oczadly.kotlin_app.hackyeah2025/
├── configuration/         # Spring configuration classes
│   ├── dynamo/           # DynamoDB client setup and properties
│   ├── CorsConfiguration.kt
│   └── SwaggerConfiguration.kt
├── demo/                 # Demo controllers
├── prompt/               # Domain module example (entity, DTO, repository, controller)
└── Hackyeah2025Application.kt
```

Domain modules follow a consistent pattern: Entity (DynamoDB bean) → Repository (Enhanced Client operations) → DTO → Controller.

## Deployment

### CI/CD Pipeline
Deployment is automated via GitHub Actions (.github/workflows/deploy.yaml):
1. Builds the application with Gradle
2. Builds multi-platform Docker images (amd64/arm64)
3. Pushes to Docker Hub with timestamp-based versioning
4. Updates ECS task definition with new image
5. Deploys to ECS Fargate cluster

**Important:** The pipeline runs on every push to `main` branch.

### Environment Variables Required
- AWS credentials are managed via IAM roles in ECS (no secrets in code)
- For local development, configure AWS CLI or set environment variables

### Docker Image Versioning
Images are tagged with both:
- `latest` - always points to most recent build
- `YYYYMMDDHHMMSS` - timestamp-based version for rollback capability

## API Documentation

Swagger UI is available at `/swagger-ui.html` when the application is running. The OpenAPI definition is configured to use relative URLs for compatibility with reverse proxies.

## Testing

The project uses JUnit 5 with Kotlin test support. Test files are located in `src/test/kotlin/`.
