package com.deploypilot.domain.checklist.dto;

import com.deploypilot.domain.checklist.ChecklistPhase;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ChecklistItemCreateRequest(
		@NotBlank
		@Size(max = 200)
		String title,

		@Size(max = 500)
		String description,

		@NotNull
		ChecklistPhase phase,

		boolean required
) {
}
