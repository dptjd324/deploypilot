package com.deploypilot.domain.health;

public interface HealthCheckClient {

	HealthCheckResponse get(String url);
}
