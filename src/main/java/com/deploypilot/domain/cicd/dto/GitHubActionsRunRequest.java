package com.deploypilot.domain.cicd.dto;

import com.deploypilot.domain.cicd.CiRunStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record GitHubActionsRunRequest(
		@NotBlank
		@Size(max = 100)
		String serviceName,

		@NotBlank
		@Size(max = 100)
		String version,

		@NotBlank
		@Size(max = 100)
		String branch,

		@NotBlank
		@Size(max = 100)
		String commitHash,

		@NotBlank
		@Size(max = 200)
		String workflowName,

		@Size(max = 100)
		String runId,

		@NotNull
		CiRunStatus status,

		@Min(0)
		Integer totalTests,

		@Min(0)
		Integer passedTests,

		@Min(0)
		Integer failedTests,

		@DecimalMin("0.0")
		BigDecimal coverage,

		@Size(max = 500)
		String logUrl,

		LocalDateTime startedAt,

		LocalDateTime finishedAt
) {
}
