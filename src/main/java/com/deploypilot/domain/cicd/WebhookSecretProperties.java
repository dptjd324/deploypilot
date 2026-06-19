package com.deploypilot.domain.cicd;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "deploypilot.webhook")
public record WebhookSecretProperties(String secret) {

	public boolean matches(String token) {
		return hasText(secret) && secret.equals(token);
	}

	private boolean hasText(String value) {
		return value != null && !value.isBlank();
	}
}
