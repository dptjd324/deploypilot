package com.deploypilot.domain.smoke.dto;

import com.deploypilot.domain.smoke.SmokeTestResult;
import com.deploypilot.domain.smoke.SmokeTestResultStatus;
import java.time.LocalDateTime;

public record SmokeTestResultResponse(
		Long id,
		Long releaseId,
		Long smokeTestId,
		String smokeTestName,
		SmokeTestResultStatus status,
		Integer actualStatus,
		Long responseTimeMs,
		String errorMessage,
		LocalDateTime executedAt
) {

	public static SmokeTestResultResponse from(SmokeTestResult result) {
		return new SmokeTestResultResponse(
				result.getId(),
				result.getRelease().getId(),
				result.getSmokeTest().getId(),
				result.getSmokeTest().getName(),
				result.getStatus(),
				result.getActualStatus(),
				result.getResponseTimeMs(),
				result.getErrorMessage(),
				result.getExecutedAt()
		);
	}
}
