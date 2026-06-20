package com.deploypilot.domain.risk.dto;

import jakarta.validation.constraints.NotBlank;

public record RiskCheckRequest(
		@NotBlank
		String targetText
) {
}
