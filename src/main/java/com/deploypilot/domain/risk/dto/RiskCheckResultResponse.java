package com.deploypilot.domain.risk.dto;

import com.deploypilot.domain.gate.CheckStatus;
import com.deploypilot.domain.gate.Severity;
import com.deploypilot.domain.risk.RiskCheckResult;
import com.deploypilot.domain.risk.RiskType;
import java.time.LocalDateTime;

public record RiskCheckResultResponse(
		Long id,
		Long releaseId,
		RiskType riskType,
		Severity severity,
		CheckStatus status,
		String message,
		String detectedPattern,
		String targetText,
		LocalDateTime checkedAt
) {

	public static RiskCheckResultResponse from(RiskCheckResult result) {
		return new RiskCheckResultResponse(
				result.getId(),
				result.getRelease().getId(),
				result.getRiskType(),
				result.getSeverity(),
				result.getStatus(),
				result.getMessage(),
				result.getDetectedPattern(),
				result.getTargetText(),
				result.getCheckedAt()
		);
	}
}
