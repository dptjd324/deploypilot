package com.deploypilot.domain.release.dto;

import com.deploypilot.domain.serviceapp.ServiceAppEnvironment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record ReleaseCreateRequest(
		@NotNull
		Long serviceAppId,

		@NotBlank
		@Size(max = 100)
		String version,

		@NotNull
		ServiceAppEnvironment environment,

		@NotBlank
		@Size(max = 100)
		String branch,

		@NotBlank
		@Size(max = 100)
		String commitHash,

		LocalDateTime commitTime
) {
}
