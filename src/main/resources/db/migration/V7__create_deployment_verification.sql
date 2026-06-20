CREATE TABLE checklist_item (
    id BIGSERIAL PRIMARY KEY,
    release_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description VARCHAR(500),
    phase VARCHAR(50) NOT NULL,
    required BOOLEAN NOT NULL,
    checked BOOLEAN NOT NULL,
    checked_by VARCHAR(100),
    checked_at TIMESTAMP,
    CONSTRAINT fk_checklist_item_release
        FOREIGN KEY (release_id)
        REFERENCES release (id)
);

CREATE TABLE health_check_result (
    id BIGSERIAL PRIMARY KEY,
    release_id BIGINT NOT NULL,
    service_app_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL,
    http_status INTEGER,
    response_time_ms BIGINT,
    response_body TEXT,
    error_message VARCHAR(500),
    checked_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_health_check_result_release
        FOREIGN KEY (release_id)
        REFERENCES release (id),
    CONSTRAINT fk_health_check_result_service_app
        FOREIGN KEY (service_app_id)
        REFERENCES service_app (id)
);

CREATE TABLE smoke_test (
    id BIGSERIAL PRIMARY KEY,
    service_app_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    method VARCHAR(20) NOT NULL,
    path VARCHAR(500) NOT NULL,
    expected_status INTEGER NOT NULL,
    max_response_time_ms BIGINT NOT NULL,
    required BOOLEAN NOT NULL,
    enabled BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_smoke_test_service_app
        FOREIGN KEY (service_app_id)
        REFERENCES service_app (id)
);

CREATE TABLE smoke_test_result (
    id BIGSERIAL PRIMARY KEY,
    release_id BIGINT NOT NULL,
    smoke_test_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL,
    actual_status INTEGER,
    response_time_ms BIGINT,
    error_message VARCHAR(500),
    executed_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_smoke_test_result_release
        FOREIGN KEY (release_id)
        REFERENCES release (id),
    CONSTRAINT fk_smoke_test_result_smoke_test
        FOREIGN KEY (smoke_test_id)
        REFERENCES smoke_test (id)
);
