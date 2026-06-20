package com.deploypilot.domain.smoke.dto;

import com.deploypilot.domain.smoke.SmokeTest;
import com.deploypilot.domain.smoke.SmokeTestMethod;
import java.time.LocalDateTime;

public record SmokeTestResponse(
		Long id,
		Long serviceAppId,
		String name,
		SmokeTestMethod method,
		String path,
		int expectedStatus,
		long maxResponseTimeMs,
		boolean required,
		boolean enabled,
		LocalDateTime createdAt,
		LocalDateTime updatedAt
) {

	public static SmokeTestResponse from(SmokeTest smokeTest) {
		return new SmokeTestResponse(
				smokeTest.getId(),
				smokeTest.getServiceApp().getId(),
				smokeTest.getName(),
				smokeTest.getMethod(),
				smokeTest.getPath(),
				smokeTest.getExpectedStatus(),
				smokeTest.getMaxResponseTimeMs(),
				smokeTest.isRequired(),
				smokeTest.isEnabled(),
				smokeTest.getCreatedAt(),
				smokeTest.getUpdatedAt()
		);
	}
}
