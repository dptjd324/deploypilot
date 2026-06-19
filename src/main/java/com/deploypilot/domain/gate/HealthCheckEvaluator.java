package com.deploypilot.domain.gate;

import com.deploypilot.domain.release.Release;
import org.springframework.stereotype.Component;

@Component
public class HealthCheckEvaluator implements QualityCheckEvaluator {

	@Override
	public QualityCheckResult evaluate(Release release) {
		return QualityCheckResult.create(
				release,
				CheckType.HEALTH_CHECK,
				CheckStatus.WARN,
				Severity.WARNING,
				"Health check module is not configured yet",
				null
		);
	}
}
