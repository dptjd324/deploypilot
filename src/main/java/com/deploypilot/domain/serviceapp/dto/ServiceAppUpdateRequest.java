package com.deploypilot.domain.serviceapp.dto;

import com.deploypilot.domain.serviceapp.ServiceAppEnvironment;
import com.deploypilot.domain.serviceapp.ServiceAppStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ServiceAppUpdateRequest(
		@NotBlank
		@Size(max = 100)
		String name,

		@Size(max = 500)
		String description,

		@NotBlank
		@Size(max = 500)
		String repositoryUrl,

		@NotBlank
		@Size(max = 100)
		String defaultBranch,

		@NotBlank
		@Size(max = 100)
		String teamName,

		@NotNull
		ServiceAppEnvironment environment,

		@Size(max = 500)
		String healthCheckUrl,

		@NotNull
		ServiceAppStatus status
) {
}
