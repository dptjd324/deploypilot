package com.deploypilot.domain.health;

public class HealthCheckClientException extends RuntimeException {

	private final long responseTimeMs;

	public HealthCheckClientException(long responseTimeMs, String message, Throwable cause) {
		super(message, cause);
		this.responseTimeMs = responseTimeMs;
	}

	public long getResponseTimeMs() {
		return responseTimeMs;
	}
}
