# Architecture - DeployPilot

## 1. Architecture Position

DeployPilot sits between CI/CD tools and deployment operations.

CI/CD tools execute pipelines.

DeployPilot collects their results and evaluates release quality.

## 2. External Integrations

### CI/CD Providers

- GitHub Actions
- Jenkins
- GitLab CI

Initial implementation will support GitHub Actions only.

### Deployment Systems

- Manual deployment status update initially
- Argo CD integration can be added later

### Metrics Providers

- Manual metrics API initially
- Datadog or Prometheus integration can be added later

## 3. Main Modules

- User/Auth Module
- Service Catalog Module
- Release Management Module
- CI/CD Result Collector
- Quality Gate Engine
- Checklist Module
- Health Check Runner
- Smoke Test Runner
- Risk Check Engine
- Metrics Module
- DORA Dashboard
- Audit Log Module

## 4. Data Flow

### CI Result Flow

GitHub Actions  
→ DeployPilot Webhook  
→ CiRun saved  
→ QualityCheckResult saved  
→ Release status updated

### Quality Gate Flow

Release  
→ Evaluators executed  
→ QualityCheckResult saved  
→ QualityGateResult decided  
→ Release gateStatus updated

### Deployment Verification Flow

Release deployed  
→ Health Check executed  
→ Smoke Test executed  
→ Metrics collected  
→ Release status updated to STABLE or FAILED

## 5. Quality Gate Rule

- If any BLOCKER check fails, Gate result is FAIL.
- If no BLOCKER fails but at least one warning exists, Gate result is WARN.
- If all checks pass, Gate result is PASS.

## 6. Package Structure

```text
com.deploypilot

├── domain
│   ├── user
│   ├── serviceapp
│   ├── release
│   ├── cicd
│   ├── gate
│   ├── checklist
│   ├── health
│   ├── smoke
│   ├── risk
│   ├── metric
│   ├── rollback
│   └── audit
│
├── global
│   ├── config
│   ├── security
│   ├── exception
│   ├── response
│   └── util
│
├── integration
│   ├── github
│   ├── jenkins
│   ├── gitlab
│   ├── argocd
│   └── metrics
│
└── web
    ├── dashboard
    ├── serviceapp
    ├── release
    ├── gate
    └── settings