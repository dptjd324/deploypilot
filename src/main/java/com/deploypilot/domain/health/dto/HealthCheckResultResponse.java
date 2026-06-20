package com.deploypilot.domain.health.dto;

import com.deploypilot.domain.health.HealthCheckResult;
import com.deploypilot.domain.health.HealthCheckStatus;
import java.time.LocalDateTime;

public record HealthCheckResultResponse(
		Long id,
		Long releaseId,
		Long serviceAppId,
		HealthCheckStatus status,
		Integer httpStatus,
		Long responseTimeMs,
		String responseBody,
		String errorMessage,
		LocalDateTime checkedAt
) {

	public static HealthCheckResultResponse from(HealthCheckResult result) {
		return new HealthCheckResultResponse(
				result.getId(),
				result.getRelease().getId(),
				result.getServiceApp().getId(),
				result.getStatus(),
				result.getHttpStatus(),
				result.getResponseTimeMs(),
				result.getResponseBody(),
				result.getErrorMessage(),
				result.getCheckedAt()
		);
	}
}
