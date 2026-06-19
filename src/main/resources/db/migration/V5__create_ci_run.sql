CREATE TABLE ci_run (
    id BIGSERIAL PRIMARY KEY,
    release_id BIGINT NOT NULL,
    provider VARCHAR(50) NOT NULL,
    workflow_name VARCHAR(200) NOT NULL,
    run_id VARCHAR(100),
    status VARCHAR(50) NOT NULL,
    total_tests INTEGER,
    passed_tests INTEGER,
    failed_tests INTEGER,
    coverage NUMERIC(5, 2),
    started_at TIMESTAMP,
    finished_at TIMESTAMP,
    duration_seconds BIGINT,
    log_url VARCHAR(500),
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_ci_run_release
        FOREIGN KEY (release_id)
        REFERENCES release (id)
);
