package com.deploypilot.domain.gate;

import com.deploypilot.domain.health.HealthCheckResult;
import com.deploypilot.domain.health.HealthCheckResultRepository;
import com.deploypilot.domain.health.HealthCheckStatus;
import com.deploypilot.domain.release.Release;
import org.springframework.stereotype.Component;

@Component
public class HealthCheckEvaluator implements QualityCheckEvaluator {

	private final HealthCheckResultRepository healthCheckResultRepository;

	public HealthCheckEvaluator(HealthCheckResultRepository healthCheckResultRepository) {
		this.healthCheckResultRepository = healthCheckResultRepository;
	}

	@Override
	public QualityCheckResult evaluate(Release release) {
		return healthCheckResultRepository.findTopByReleaseIdOrderByCheckedAtDesc(release.getId())
				.map(result -> evaluateResult(release, result))
				.orElseGet(() -> QualityCheckResult.create(
						release,
						CheckType.HEALTH_CHECK,
						CheckStatus.WARN,
						Severity.WARNING,
						"No health check result found",
						null
				));
	}

	private QualityCheckResult evaluateResult(Release release, HealthCheckResult result) {
		if (result.getStatus() == HealthCheckStatus.PASS) {
			return QualityCheckResult.create(
					release,
					CheckType.HEALTH_CHECK,
					CheckStatus.PASS,
					Severity.INFO,
					"Latest health check passed",
					null
			);
		}

		return QualityCheckResult.create(
				release,
				CheckType.HEALTH_CHECK,
				CheckStatus.FAIL,
				Severity.BLOCKER,
				"Latest health check failed",
				"{\"errorMessage\":\"" + result.getErrorMessage() + "\"}"
		);
	}
}
