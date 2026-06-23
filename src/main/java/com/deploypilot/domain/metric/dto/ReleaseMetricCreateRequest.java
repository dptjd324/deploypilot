package com.deploypilot.domain.metric.dto;

import jakarta.validation.constraints.Min;

public record ReleaseMetricCreateRequest(
		@Min(0)
		long totalRequests,

		@Min(0)
		long error5xxCount,

		@Min(0)
		long avgResponseTimeMs,

		@Min(0)
		long p95ResponseTimeMs
) {
}
