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

- [ ] Add QualityCheckResult entity
- [ ] Add QualityGateResult entity
- [ ] Add CheckType enum
- [ ] Add CheckStatus enum
- [ ] Add Severity enum
- [ ] Add GateResult enum

## Phase 7 - Quality Gate Engine

- [ ] Add QualityCheckEvaluator interface
- [ ] Add BuildCheckEvaluator
- [ ] Add TestCheckEvaluator
- [ ] Add CoverageCheckEvaluator
- [ ] Add ChecklistEvaluator
- [ ] Add HealthCheckEvaluator
- [ ] Add SmokeTestEvaluator
- [ ] Add QualityGateEngine
- [ ] Add tests for gate decision rules

## Phase 8 - Deployment Verification

- [ ] Add ChecklistItem
- [ ] Add HealthCheckResult
- [ ] Add Health Check Runner
- [ ] Add SmokeTest
- [ ] Add SmokeTestResult
- [ ] Add Smoke Test Runner

## Phase 9 - Risk Check

- [ ] Add RiskCheckResult
- [ ] Add DB Migration Risk Check
- [ ] Add Security Config Risk Check

## Phase 10 - Post Deploy Metrics

- [ ] Add ReleaseMetric
- [ ] Add metrics collection API
- [ ] Evaluate error rate
- [ ] Evaluate response time

## Phase 11 - Rollback

- [ ] Add RollbackRecord
- [ ] Add rollback record API
- [ ] Update release status to ROLLED_BACK
- [ ] Add audit log

## Phase 12 - DORA Dashboard

- [ ] Add deployment frequency calculation
- [ ] Add lead time for changes calculation
- [ ] Add change failure rate calculation
- [ ] Add failed deployment recovery time calculation
