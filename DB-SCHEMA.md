# DB Schema - DeployPilot

## Core Tables

### users

- id
- email
- password
- name
- role
- created_at
- updated_at

### service_app

- id
- name
- description
- repository_url
- default_branch
- team_name
- environment
- health_check_url
- status
- created_at
- updated_at

Environment values:

- DEV
- STAGING
- PRODUCTION

Status values:

- ACTIVE
- INACTIVE

### release

- id
- service_app_id
- version
- environment
- branch
- commit_hash
- commit_time
- status
- gate_status
- created_at
- ci_started_at
- ci_finished_at
- deployed_at
- monitoring_started_at
- stable_at
- failed_at
- rolled_back_at

Release status values:

- CREATED
- CI_RUNNING
- CI_SUCCESS
- CI_FAILED
- APPROVED
- DEPLOYED
- STABLE
- FAILED
- ROLLED_BACK

Gate status values:

- PENDING
- PASS
- WARN
- FAIL

### ci_run

- id
- release_id
- provider
- workflow_name
- run_id
- status
- total_tests
- passed_tests
- failed_tests
- coverage
- started_at
- finished_at
- duration_seconds
- log_url
- created_at

Provider values:

- GITHUB_ACTIONS

Status values:

- IN_PROGRESS
- SUCCESS
- FAILURE
- CANCELLED

### quality_check_result

- id
- release_id
- check_type
- status
- severity
- message
- detail_json
- checked_at

Check type values:

- BUILD
- TEST
- COVERAGE
- CHECKLIST
- HEALTH_CHECK
- SMOKE_TEST
- DB_MIGRATION_RISK
- SECURITY_CONFIG_RISK
- METRIC_ERROR_RATE
- METRIC_RESPONSE_TIME

Check status values:

- PASS
- WARN
- FAIL

Severity values:

- INFO
- WARNING
- BLOCKER

### quality_gate_result

- id
- release_id
- result
- reason
- checked_at

Gate result values:

- PASS
- WARN
- FAIL

### checklist_item

- id
- release_id
- title
- description
- phase
- required
- checked
- checked_by
- checked_at

Checklist phase values:

- PRE_DEPLOY
- DEPLOY
- POST_DEPLOY

### smoke_test

- id
- service_app_id
- name
- method
- path
- expected_status
- max_response_time_ms
- required
- enabled
- created_at
- updated_at

Smoke test method values:

- GET
- POST
- PUT
- PATCH
- DELETE

### smoke_test_result

- id
- release_id
- smoke_test_id
- status
- actual_status
- response_time_ms
- error_message
- executed_at

Smoke test result status values:

- PASS
- FAIL

### health_check_result

- id
- release_id
- service_app_id
- status
- http_status
- response_time_ms
- response_body
- error_message
- checked_at

Health check status values:

- PASS
- FAIL

### risk_check_result

- id
- release_id
- risk_type
- severity
- status
- message
- detected_pattern
- target_text
- checked_at

Risk type values:

- DB_MIGRATION
- SECURITY_CONFIG

Risk check status values:

- PASS
- WARN
- FAIL

Risk severity values:

- INFO
- WARNING
- BLOCKER

### release_metric

- id
- release_id
- total_requests
- error_5xx_count
- error_rate
- avg_response_time_ms
- p95_response_time_ms
- collected_at

Metric evaluation rule:

- Error rate >= 5.00 is BLOCKER FAIL
- Error rate >= 1.00 and < 5.00 is WARNING WARN
- P95 response time >= 3000ms is BLOCKER FAIL
- P95 response time >= 1000ms and < 3000ms is WARNING WARN

### rollback_record

- id
- release_id
- target_version
- reason
- created_by
- created_at

### audit_log

- id
- actor_id
- action
- target_type
- target_id
- message
- ip_address
- created_at
