# AGENTS.md

## Project

DeployPilot is a Spring Boot based Release Quality Gate Platform.

It collects CI/CD results, health check results, smoke test results, risk check results, and post-deploy metrics.

It evaluates release quality at the release level.

## What DeployPilot Is Not

DeployPilot does not replace GitHub Actions, Jenkins, GitLab CI, Argo CD, or Datadog.

DeployPilot is not a CI/CD executor.

DeployPilot is not a monitoring platform.

DeployPilot is not a Kubernetes deployment controller.

## Tech Stack

- Java 21
- Spring Boot 3.x
- Gradle
- Spring Data JPA
- Spring Security
- PostgreSQL
- Flyway
- Docker Compose

## Package Rules

Use this package structure:

```text
com.deploypilot
├── domain
├── global
├── integration
└── web

Domain-specific logic must stay inside the domain package.

Coding Rules
Do not implement unrelated features.
Do not change the project direction.
Do not add external services unless explicitly requested.
Do not put business logic in controllers.
Use DTOs for request and response.
Use enums for fixed statuses.
Use services for transactional business logic.
Add validation for request DTOs.
Add tests for important domain rules.
Update documentation when APIs or database schema change.
Architecture Rules
Quality Gate checks must be implemented as separate Evaluator classes.
Do not create one large if-else based gate service.
CI providers must be separated by provider type.
GitHub Actions integration is the first priority.
Jenkins and GitLab CI integration should be left for future extension unless requested.
Health Check and Smoke Test results must be stored per release.
Every Quality Gate decision must be traceable through QualityCheckResult.
Security Rules
Never commit secrets.
Do not hardcode tokens.
Webhook APIs must support secret header validation.
Use BCrypt for passwords.
Sensitive operations should create AuditLog entries.
Documentation Rules

When changing APIs, update API-SPEC.md.

When changing database schema, update DB-SCHEMA.md.

When changing architecture, update ARCHITECTURE.md.