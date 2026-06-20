package com.deploypilot.domain.gate;

import com.deploypilot.domain.release.Release;
import com.deploypilot.domain.smoke.SmokeTestResultRepository;
import com.deploypilot.domain.smoke.SmokeTestResultStatus;
import org.springframework.stereotype.Component;

@Component
public class SmokeTestEvaluator implements QualityCheckEvaluator {

	private final SmokeTestResultRepository smokeTestResultRepository;

	public SmokeTestEvaluator(SmokeTestResultRepository smokeTestResultRepository) {
		this.smokeTestResultRepository = smokeTestResultRepository;
	}

	@Override
	public QualityCheckResult evaluate(Release release) {
		long totalCount = smokeTestResultRepository.countByReleaseId(release.getId());
		if (totalCount == 0) {
			return QualityCheckResult.create(
					release,
					CheckType.SMOKE_TEST,
					CheckStatus.WARN,
					Severity.WARNING,
					"No smoke test result found",
					null
			);
		}

		long failedCount = smokeTestResultRepository.countByReleaseIdAndStatus(release.getId(), SmokeTestResultStatus.FAIL);
		if (failedCount == 0) {
			return QualityCheckResult.create(
					release,
					CheckType.SMOKE_TEST,
					CheckStatus.PASS,
					Severity.INFO,
					"Smoke tests passed",
					null
			);
		}

		return QualityCheckResult.create(
				release,
				CheckType.SMOKE_TEST,
				CheckStatus.FAIL,
				Severity.BLOCKER,
				"Smoke tests failed",
				"{\"failedCount\":" + failedCount + "}"
		);
	}
}
