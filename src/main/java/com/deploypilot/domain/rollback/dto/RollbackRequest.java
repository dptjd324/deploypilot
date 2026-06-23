package com.deploypilot.domain.rollback.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RollbackRequest(
		@NotBlank
		@Size(max = 100)
		String targetVersion,

		@NotBlank
		@Size(max = 500)
		String reason,

		@NotNull
		Long createdBy,

		@Size(max = 100)
		String ipAddress
) {
}
