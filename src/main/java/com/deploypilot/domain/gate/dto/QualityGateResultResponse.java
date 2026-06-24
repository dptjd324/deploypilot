package com.deploypilot.domain.gate.dto;

import com.deploypilot.domain.gate.GateResult;
import com.deploypilot.domain.gate.QualityGateResult;
import java.time.LocalDateTime;

public record QualityGateResultResponse(
		Long id,
		Long releaseId,
		GateResult result,
		String reason,
		LocalDateTime checkedAt
) {

	public static QualityGateResultResponse from(QualityGateResult result) {
		return new QualityGateResultResponse(
				result.getId(),
				result.getRelease().getId(),
				result.getResult(),
				result.getReason(),
				result.getCheckedAt()
		);
	}
}
