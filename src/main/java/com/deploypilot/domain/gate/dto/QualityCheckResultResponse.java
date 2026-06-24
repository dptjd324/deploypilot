package com.deploypilot.domain.gate.dto;

import com.deploypilot.domain.gate.CheckStatus;
import com.deploypilot.domain.gate.CheckType;
import com.deploypilot.domain.gate.QualityCheckResult;
import com.deploypilot.domain.gate.Severity;
import java.time.LocalDateTime;

public record QualityCheckResultResponse(
		Long id,
		Long releaseId,
		CheckType checkType,
		CheckStatus status,
		Severity severity,
		String message,
		String detailJson,
		LocalDateTime checkedAt
) {

	public static QualityCheckResultResponse from(QualityCheckResult result) {
		return new QualityCheckResultResponse(
				result.getId(),
				result.getRelease().getId(),
				result.getCheckType(),
				result.getStatus(),
				result.getSeverity(),
				result.getMessage(),
				result.getDetailJson(),
				result.getCheckedAt()
		);
	}
}
