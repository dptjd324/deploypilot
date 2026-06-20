package com.deploypilot.domain.smoke;

public class SmokeTestClientException extends RuntimeException {

	private final long responseTimeMs;

	public SmokeTestClientException(long responseTimeMs, String message, Throwable cause) {
		super(message, cause);
		this.responseTimeMs = responseTimeMs;
	}

	public long getResponseTimeMs() {
		return responseTimeMs;
	}
}
