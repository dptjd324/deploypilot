package com.deploypilot.domain.dashboard.dto;

import java.time.LocalDateTime;

public record DoraDashboardResponse(
		LocalDateTime from,
		LocalDateTime to,
		long deploymentFrequency
) {
}
