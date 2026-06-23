CREATE TABLE rollback_record (
    id BIGSERIAL PRIMARY KEY,
    release_id BIGINT NOT NULL,
    target_version VARCHAR(100) NOT NULL,
    reason VARCHAR(500) NOT NULL,
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_rollback_record_release
        FOREIGN KEY (release_id)
        REFERENCES release (id),
    CONSTRAINT fk_rollback_record_user
        FOREIGN KEY (created_by)
        REFERENCES users (id)
);

CREATE TABLE audit_log (
    id BIGSERIAL PRIMARY KEY,
    actor_id BIGINT NOT NULL,
    action VARCHAR(100) NOT NULL,
    target_type VARCHAR(100) NOT NULL,
    target_id BIGINT NOT NULL,
    message VARCHAR(500) NOT NULL,
    ip_address VARCHAR(100),
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_audit_log_actor
        FOREIGN KEY (actor_id)
        REFERENCES users (id)
);
