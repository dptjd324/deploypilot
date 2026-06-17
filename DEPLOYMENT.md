# Deployment - DeployPilot

## Local Development

Planned local environment:

- Spring Boot application
- PostgreSQL via Docker Compose

## Environment Variables

```text
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/deploypilot
SPRING_DATASOURCE_USERNAME=deploypilot
SPRING_DATASOURCE_PASSWORD=deploypilot
DEPLOYPILOT_WEBHOOK_SECRET=local-secret

Docker Compose

Docker Compose will be used for PostgreSQL.

CI

GitHub Actions will run:

Gradle build
Tests
Static checks if added later
Production Deployment

Production deployment is out of scope for the initial version.