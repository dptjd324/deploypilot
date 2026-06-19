CREATE TABLE quality_check_result (
    id BIGSERIAL PRIMARY KEY,
    release_id BIGINT NOT NULL,
    check_type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    severity VARCHAR(50) NOT NULL,
    message VARCHAR(500) NOT NULL,
    detail_json TEXT,
    checked_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_quality_check_result_release
        FOREIGN KEY (release_id)
        REFERENCES release (id)
);

CREATE TABLE quality_gate_result (
    id BIGSERIAL PRIMARY KEY,
    release_id BIGINT NOT NULL,
    result VARCHAR(50) NOT NULL,
    reason VARCHAR(500) NOT NULL,
    checked_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_quality_gate_result_release
        FOREIGN KEY (release_id)
        REFERENCES release (id)
);
