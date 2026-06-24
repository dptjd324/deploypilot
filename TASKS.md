# Tasks - DeployPilot

## Phase 2 - Auth

- [x] Add User entity
- [x] Add Role enum
- [x] Configure Spring Security
- [x] Add BCrypt password encoding
- [x] Add admin seed user

## Phase 3 - Service Catalog

- [x] Add ServiceApp entity
- [x] Add ServiceApp CRUD API
- [x] Add validation
- [x] Add service list/detail screen or API

## Phase 4 - Release Management

- [x] Add Release entity
- [x] Add ReleaseStatus enum
- [x] Add GateStatus enum
- [x] Add release creation API
- [x] Add release detail API
- [x] Add release status transition logic

## Phase 5 - GitHub Actions Integration

- [x] Add CiRun entity
- [x] Add CI provider enum
- [x] Add GitHub Actions webhook API
- [x] Add webhook secret validation
- [x] Save CI result
- [x] Update release status from CI result

## Phase 6 - Quality Gate Model

- [x] Add QualityCheckResult entity
- [x] Add QualityGateResult entity
- [x] Add CheckType enum
- [x] Add CheckStatus enum
- [x] Add Severity enum
- [x] Add GateResult enum

## Phase 7 - Quality Gate Engine

- [x] Add QualityCheckEvaluator interface
- [x] Add BuildCheckEvaluator
- [x] Add TestCheckEvaluator
- [x] Add CoverageCheckEvaluator
- [x] Add ChecklistEvaluator
- [x] Add HealthCheckEvaluator
- [x] Add SmokeTestEvaluator
- [x] Add QualityGateEngine
- [x] Add tests for gate decision rules

## Phase 8 - Deployment Verification

- [x] Add ChecklistItem
- [x] Add HealthCheckResult
- [x] Add Health Check Runner
- [x] Add SmokeTest
- [x] Add SmokeTestResult
- [x] Add Smoke Test Runner

## Phase 9 - Risk Check

- [x] Add RiskCheckResult
- [x] Add DB Migration Risk Check
- [x] Add Security Config Risk Check

## Phase 10 - Post Deploy Metrics

- [x] Add ReleaseMetric
- [x] Add metrics collection API
- [x] Evaluate error rate
- [x] Evaluate response time

## Phase 11 - Rollback

- [x] Add RollbackRecord
- [x] Add rollback record API
- [x] Update release status to ROLLED_BACK
- [x] Add audit log

## Phase 12 - DORA Dashboard

- [x] Add deployment frequency calculation
- [x] Add lead time for changes calculation
- [x] Add change failure rate calculation
- [x] Add failed deployment recovery time calculation
