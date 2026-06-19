package com.deploypilot.domain.gate;

import com.deploypilot.domain.release.Release;
import org.springframework.stereotype.Component;

@Component
public class SmokeTestEvaluator implements QualityCheckEvaluator {

	@Override
	public QualityCheckResult evaluate(Release release) {
		return QualityCheckResult.create(
				release,
				CheckType.SMOKE_TEST,
				CheckStatus.WARN,
				Severity.WARNING,
				"Smoke test module is not configured yet",
				null
		);
	}
}
