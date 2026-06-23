package com.deploypilot.domain.metric.dto;

import com.deploypilot.domain.metric.ReleaseMetric;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReleaseMetricResponse(
		Long id,
		Long releaseId,
		long totalRequests,
		long error5xxCount,
		BigDecimal errorRate,
		long avgResponseTimeMs,
		long p95ResponseTimeMs,
		LocalDateTime collectedAt
) {

	public static ReleaseMetricResponse from(ReleaseMetric metric) {
		return new ReleaseMetricResponse(
				metric.getId(),
				metric.getRelease().getId(),
				metric.getTotalRequests(),
				metric.getError5xxCount(),
				metric.getErrorRate(),
				metric.getAvgResponseTimeMs(),
				metric.getP95ResponseTimeMs(),
				metric.getCollectedAt()
		);
	}
}
