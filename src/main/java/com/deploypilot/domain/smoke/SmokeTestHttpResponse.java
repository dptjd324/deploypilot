package com.deploypilot.domain.smoke;

public record SmokeTestHttpResponse(
		int httpStatus,
		long responseTimeMs
) {
}
