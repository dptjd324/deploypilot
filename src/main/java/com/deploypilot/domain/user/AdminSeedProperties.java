package com.deploypilot.domain.user;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "deploypilot.admin")
public record AdminSeedProperties(
		String email,
		String password,
		String name
) {

	public boolean canSeed() {
		return hasText(email) && hasText(password);
	}

	public String seedName() {
		return hasText(name) ? name : "DeployPilot Admin";
	}

	private boolean hasText(String value) {
		return value != null && !value.isBlank();
	}
}
