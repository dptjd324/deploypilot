CREATE TABLE release_metric (
    id BIGSERIAL PRIMARY KEY,
    release_id BIGINT NOT NULL,
    total_requests BIGINT NOT NULL,
    error_5xx_count BIGINT NOT NULL,
    error_rate NUMERIC(6, 2) NOT NULL,
    avg_response_time_ms BIGINT NOT NULL,
    p95_response_time_ms BIGINT NOT NULL,
    collected_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_release_metric_release
        FOREIGN KEY (release_id)
        REFERENCES release (id)
);
