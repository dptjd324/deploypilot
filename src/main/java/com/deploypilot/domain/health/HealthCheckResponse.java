package com.deploypilot.domain.health;

public record HealthCheckResponse(
		int httpStatus,
		long responseTimeMs,
		String responseBody
) {
}
