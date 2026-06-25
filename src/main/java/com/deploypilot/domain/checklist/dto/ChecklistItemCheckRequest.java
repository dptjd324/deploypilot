package com.deploypilot.domain.checklist.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChecklistItemCheckRequest(
		@NotBlank
		@Size(max = 100)
		String checkedBy
) {
}
