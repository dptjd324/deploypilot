package com.deploypilot.domain.smoke.dto;

import com.deploypilot.domain.smoke.SmokeTestMethod;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SmokeTestUpdateRequest(
		@NotBlank
		@Size(max = 100)
		String name,

		@NotNull
		SmokeTestMethod method,

		@NotBlank
		@Size(max = 500)
		String path,

		@Min(100)
		@Max(599)
		int expectedStatus,

		@Min(1)
		long maxResponseTimeMs,

		boolean required,

		boolean enabled
) {
}
