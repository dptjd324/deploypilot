package com.deploypilot.domain.dashboard.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DoraDashboardResponse(
		LocalDateTime from,
		LocalDateTime to,
		long deploymentFrequency,
		long averageLeadTimeMinutes,
		BigDecimal changeFailureRate
) {
}
