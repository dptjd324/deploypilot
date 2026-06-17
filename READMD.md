# DeployPilot

DeployPilot is a Spring Boot based Release Quality Gate Platform.

It does not replace CI/CD tools such as GitHub Actions, Jenkins, GitLab CI, Argo CD, or observability tools such as Datadog.

Instead, it collects results from those tools and evaluates release quality at the release level.

## Core Concept

DeployPilot answers one question:

> Is this release safe enough to deploy?

To answer that, DeployPilot collects and evaluates:

- CI build result
- Test result
- Test coverage
- Deployment checklist
- Health check result
- Smoke test result
- DB migration risk
- Security config risk
- Post-deploy metrics
- Rollback history
- DORA metrics

## Tech Stack

- Java 21
- Spring Boot 3.x
- Spring Data JPA
- Spring Security
- PostgreSQL
- Flyway
- Docker Compose
- GitHub Actions

## What DeployPilot Is Not

DeployPilot is not a CI/CD execution engine.

DeployPilot is not a Kubernetes deployment controller.

DeployPilot is not a monitoring platform.

DeployPilot is a release quality evaluation layer between CI/CD tools and deployment operations.

## Planned Features

- Service Catalog
- Release Management
- GitHub Actions result collection
- Quality Gate Engine
- Health Check Runner
- Smoke Test Runner
- DB Migration Risk Check
- Security Config Risk Check
- Post-deploy Metrics
- DORA Metrics Dashboard