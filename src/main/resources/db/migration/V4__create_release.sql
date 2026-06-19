CREATE TABLE release (
    id BIGSERIAL PRIMARY KEY,
    service_app_id BIGINT NOT NULL,
    version VARCHAR(100) NOT NULL,
    environment VARCHAR(50) NOT NULL,
    branch VARCHAR(100) NOT NULL,
    commit_hash VARCHAR(100) NOT NULL,
    commit_time TIMESTAMP,
    status VARCHAR(50) NOT NULL,
    gate_status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    ci_started_at TIMESTAMP,
    ci_finished_at TIMESTAMP,
    deployed_at TIMESTAMP,
    monitoring_started_at TIMESTAMP,
    stable_at TIMESTAMP,
    failed_at TIMESTAMP,
    rolled_back_at TIMESTAMP,
    CONSTRAINT fk_release_service_app
        FOREIGN KEY (service_app_id)
        REFERENCES service_app (id)
);
