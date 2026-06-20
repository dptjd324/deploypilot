CREATE TABLE risk_check_result (
    id BIGSERIAL PRIMARY KEY,
    release_id BIGINT NOT NULL,
    risk_type VARCHAR(50) NOT NULL,
    severity VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    message VARCHAR(500) NOT NULL,
    detected_pattern VARCHAR(200),
    target_text TEXT,
    checked_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_risk_check_result_release
        FOREIGN KEY (release_id)
        REFERENCES release (id)
);
