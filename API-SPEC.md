# API Spec - DeployPilot

## Auth

```http
POST /api/auth/login
POST /api/auth/logout
GET /api/auth/me


Service Catalog
GET /api/services
POST /api/services
GET /api/services/{serviceId}
PUT /api/services/{serviceId}
DELETE /api/services/{serviceId}

Create Request
{
  "name": "order-api",
  "description": "Order service",
  "repositoryUrl": "https://github.com/example/order-api",
  "defaultBranch": "main",
  "teamName": "platform",
  "environment": "PRODUCTION",
  "healthCheckUrl": "https://order.example.com/health"
}

Update Request
{
  "name": "order-api",
  "description": "Order service",
  "repositoryUrl": "https://github.com/example/order-api",
  "defaultBranch": "main",
  "teamName": "platform",
  "environment": "PRODUCTION",
  "healthCheckUrl": "https://order.example.com/health",
  "status": "ACTIVE"
}

Response
{
  "id": 1,
  "name": "order-api",
  "description": "Order service",
  "repositoryUrl": "https://github.com/example/order-api",
  "defaultBranch": "main",
  "teamName": "platform",
  "environment": "PRODUCTION",
  "healthCheckUrl": "https://order.example.com/health",
  "status": "ACTIVE",
  "createdAt": "2026-06-17T10:00:00",
  "updatedAt": "2026-06-17T10:00:00"
}


Releases
GET /api/releases
POST /api/releases
GET /api/releases/{releaseId}
POST /api/releases/{releaseId}/approve
POST /api/releases/{releaseId}/deploy
POST /api/releases/{releaseId}/rollback

GitHub Actions Integration
POST /api/integrations/github-actions/runs


Headers
X-DeployPilot-Token: {secret}

Request Example
{
  "serviceName": "order-api",
  "version": "v1.4.0",
  "branch": "main",
  "commitHash": "a8f3d91",
  "workflowName": "CI",
  "status": "SUCCESS",
  "totalTests": 120,
  "passedTests": 120,
  "failedTests": 0,
  "coverage": 78.4,
  "logUrl": "https://github.com/example/order-api/actions/runs/123",
  "startedAt": "2026-06-16T10:00:00",
  "finishedAt": "2026-06-16T10:03:30"
}


Quality Gate
POST /api/releases/{releaseId}/quality-gate/run
GET /api/releases/{releaseId}/quality-gate
GET /api/releases/{releaseId}/quality-checks


Health Check
POST /api/releases/{releaseId}/health-check/run
GET /api/releases/{releaseId}/health-checks


Smoke Test
GET /api/services/{serviceId}/smoke-tests
POST /api/services/{serviceId}/smoke-tests
PUT /api/services/{serviceId}/smoke-tests/{smokeTestId}
POST /api/releases/{releaseId}/smoke-tests/run
GET /api/releases/{releaseId}/smoke-test-results


Risk Check
POST /api/releases/{releaseId}/risk-check/db-migration
POST /api/releases/{releaseId}/risk-check/security-config
GET /api/releases/{releaseId}/risk-checks


Metrics
POST /api/releases/{releaseId}/metrics
GET /api/dashboard/dora
GET /api/dashboard/releases
GET /api/dashboard/services/{serviceId}
