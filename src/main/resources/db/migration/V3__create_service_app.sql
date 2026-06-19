CREATE TABLE service_app (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(500),
    repository_url VARCHAR(500) NOT NULL,
    default_branch VARCHAR(100) NOT NULL,
    team_name VARCHAR(100) NOT NULL,
    environment VARCHAR(50) NOT NULL,
    health_check_url VARCHAR(500),
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
