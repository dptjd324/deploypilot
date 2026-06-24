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
GET /api/releases/{releaseId}/rollback-records

Create Request
{
  "serviceAppId": 1,
  "version": "v1.0.0",
  "environment": "PRODUCTION",
  "branch": "main",
  "commitHash": "abc123",
  "commitTime": "2026-06-19T10:00:00"
}

Response
{
  "id": 1,
  "serviceAppId": 1,
  "serviceName": "order-api",
  "version": "v1.0.0",
  "environment": "PRODUCTION",
  "branch": "main",
  "commitHash": "abc123",
  "commitTime": "2026-06-19T10:00:00",
  "status": "CREATED",
  "gateStatus": "PENDING",
  "createdAt": "2026-06-19T10:01:00",
  "ciStartedAt": null,
  "ciFinishedAt": null,
  "deployedAt": null,
  "monitoringStartedAt": null,
  "stableAt": null,
  "failedAt": null,
  "rolledBackAt": null
}

Rollback Request
{
  "targetVersion": "v0.9.0",
  "reason": "High error rate after deploy",
  "createdBy": 1,
  "ipAddress": "127.0.0.1"
}

Rollback Response
{
  "id": 1,
  "releaseId": 1,
  "targetVersion": "v0.9.0",
  "reason": "High error rate after deploy",
  "createdBy": 1,
  "createdByEmail": "admin@example.com",
  "createdByName": "Admin",
  "releaseStatus": "ROLLED_BACK",
  "createdAt": "2026-06-23T10:00:00"
}

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
  "runId": "123",
  "status": "SUCCESS",
  "totalTests": 120,
  "passedTests": 120,
  "failedTests": 0,
  "coverage": 78.4,
  "logUrl": "https://github.com/example/order-api/actions/runs/123",
  "startedAt": "2026-06-16T10:00:00",
  "finishedAt": "2026-06-16T10:03:30"
}

Response Example
{
  "id": 1,
  "releaseId": 1,
  "provider": "GITHUB_ACTIONS",
  "workflowName": "CI",
  "runId": "123",
  "status": "SUCCESS",
  "totalTests": 120,
  "passedTests": 120,
  "failedTests": 0,
  "coverage": 78.40,
  "startedAt": "2026-06-16T10:00:00",
  "finishedAt": "2026-06-16T10:03:30",
  "durationSeconds": 210,
  "logUrl": "https://github.com/example/order-api/actions/runs/123",
  "createdAt": "2026-06-16T10:03:31"
}


Quality Gate
POST /api/releases/{releaseId}/quality-gate/run
GET /api/releases/{releaseId}/quality-gate
GET /api/releases/{releaseId}/quality-checks

Quality Gate Response
{
  "id": 1,
  "releaseId": 1,
  "result": "PASS",
  "reason": "All quality checks passed",
  "checkedAt": "2026-06-24T10:00:00"
}

Quality Check Response
{
  "id": 1,
  "releaseId": 1,
  "checkType": "BUILD",
  "status": "PASS",
  "severity": "INFO",
  "message": "Build passed",
  "detailJson": null,
  "checkedAt": "2026-06-24T10:00:00"
}


Health Check
POST /api/releases/{releaseId}/health-check/run
GET /api/releases/{releaseId}/health-checks

Response Example
{
  "id": 1,
  "releaseId": 1,
  "serviceAppId": 1,
  "status": "PASS",
  "httpStatus": 200,
  "responseTimeMs": 120,
  "responseBody": "ok",
  "errorMessage": null,
  "checkedAt": "2026-06-20T10:00:00"
}


Smoke Test
GET /api/services/{serviceId}/smoke-tests
POST /api/services/{serviceId}/smoke-tests
PUT /api/services/{serviceId}/smoke-tests/{smokeTestId}
POST /api/releases/{releaseId}/smoke-tests/run
GET /api/releases/{releaseId}/smoke-test-results

Create or Update Request
{
  "name": "Root endpoint",
  "method": "GET",
  "path": "/",
  "expectedStatus": 200,
  "maxResponseTimeMs": 500,
  "required": true,
  "enabled": true
}

Smoke Test Response
{
  "id": 1,
  "serviceAppId": 1,
  "name": "Root endpoint",
  "method": "GET",
  "path": "/",
  "expectedStatus": 200,
  "maxResponseTimeMs": 500,
  "required": true,
  "enabled": true,
  "createdAt": "2026-06-20T10:00:00",
  "updatedAt": "2026-06-20T10:00:00"
}

Smoke Test Result Response
{
  "id": 1,
  "releaseId": 1,
  "smokeTestId": 1,
  "smokeTestName": "Root endpoint",
  "status": "PASS",
  "actualStatus": 200,
  "responseTimeMs": 100,
  "errorMessage": null,
  "executedAt": "2026-06-20T10:01:00"
}


Risk Check
POST /api/releases/{releaseId}/risk-check/db-migration
POST /api/releases/{releaseId}/risk-check/security-config
GET /api/releases/{releaseId}/risk-checks

Request
{
  "targetText": "ALTER TABLE orders DROP COLUMN legacy_code;"
}

Response
{
  "id": 1,
  "releaseId": 1,
  "riskType": "DB_MIGRATION",
  "severity": "BLOCKER",
  "status": "FAIL",
  "message": "DB migration contains destructive operation",
  "detectedPattern": "DROP COLUMN",
  "targetText": "ALTER TABLE orders DROP COLUMN legacy_code;",
  "checkedAt": "2026-06-20T10:00:00"
}


Metrics
POST /api/releases/{releaseId}/metrics
GET /api/releases/{releaseId}/metrics
GET /api/dashboard/dora?from=2026-06-01T00:00:00&to=2026-06-30T23:59:59
GET /api/dashboard/releases
GET /api/dashboard/services/{serviceId}

Request
{
  "totalRequests": 1000,
  "error5xxCount": 10,
  "avgResponseTimeMs": 120,
  "p95ResponseTimeMs": 450
}

Response
{
  "id": 1,
  "releaseId": 1,
  "totalRequests": 1000,
  "error5xxCount": 10,
  "errorRate": 1.00,
  "avgResponseTimeMs": 120,
  "p95ResponseTimeMs": 450,
  "collectedAt": "2026-06-23T10:00:00"
}

DORA Dashboard Response
{
  "from": "2026-06-01T00:00:00",
  "to": "2026-06-30T23:59:59",
  "deploymentFrequency": 12,
  "averageLeadTimeMinutes": 135,
  "changeFailureRate": 16.67,
  "averageFailedDeploymentRecoveryTimeMinutes": 90
}
