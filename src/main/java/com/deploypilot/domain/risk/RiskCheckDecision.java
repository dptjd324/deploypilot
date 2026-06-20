package com.deploypilot.domain.risk;

import com.deploypilot.domain.gate.CheckStatus;
import com.deploypilot.domain.gate.Severity;

public record RiskCheckDecision(
		Severity severity,
		CheckStatus status,
		String message,
		String detectedPattern
) {
}
