# PRD - DeployPilot

## 1. Problem

Modern development teams use many tools during release:

- GitHub Actions for CI
- Jenkins or GitLab CI for pipelines
- Argo CD for deployment synchronization
- Datadog or Prometheus for monitoring
- Manual checklist documents for release readiness

Each tool provides its own result, but it is difficult to answer the release-level question:

> Is this release safe enough to deploy?

## 2. Goal

DeployPilot aims to collect release-related signals and evaluate release quality in one place.

## 3. Target Users

- Backend developers
- DevOps engineers
- Platform engineers
- Small teams without a dedicated release platform

## 4. Core Value

DeployPilot provides a release-level quality gate by combining:

- CI result
- Test result
- Manual checklist
- Health check
- Smoke test
- Risk checks
- Post-deploy metrics

## 5. MVP Scope

- User authentication
- Service catalog
- Release management
- GitHub Actions webhook
- Quality gate engine
- Health check
- Smoke test
- Checklist
- DORA metrics

## 6. Out of Scope

- Replacing GitHub Actions, Jenkins, GitLab CI, Argo CD, or Datadog
- Running actual production deployments
- Full Kubernetes orchestration
- Full observability platform
- Secret management platform