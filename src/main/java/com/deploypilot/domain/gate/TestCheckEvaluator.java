package com.deploypilot.domain.gate;

import com.deploypilot.domain.cicd.CiRun;
import com.deploypilot.domain.cicd.CiRunRepository;
import com.deploypilot.domain.release.Release;
import org.springframework.stereotype.Component;

@Component
public class TestCheckEvaluator implements QualityCheckEvaluator {

	private final CiRunRepository ciRunRepository;

	public TestCheckEvaluator(CiRunRepository ciRunRepository) {
		this.ciRunRepository = ciRunRepository;
	}

	@Override
	public QualityCheckResult evaluate(Release release) {
		return ciRunRepository.findTopByReleaseIdOrderByCreatedAtDesc(release.getId())
				.map(ciRun -> evaluateCiRun(release, ciRun))
				.orElseGet(() -> QualityCheckResult.create(
						release,
						CheckType.TEST,
						CheckStatus.WARN,
						Severity.WARNING,
						"No test result found",
						null
				));
	}

	private QualityCheckResult evaluateCiRun(Release release, CiRun ciRun) {
		Integer failedTests = ciRun.getFailedTests();
		if (failedTests == null) {
			return QualityCheckResult.create(
					release,
					CheckType.TEST,
					CheckStatus.WARN,
					Severity.WARNING,
					"Test count was not reported",
					null
			);
		}

		if (failedTests > 0) {
			return QualityCheckResult.create(
					release,
					CheckType.TEST,
					CheckStatus.FAIL,
					Severity.BLOCKER,
					"Tests failed",
					"{\"failedTests\":" + failedTests + "}"
			);
		}

		return QualityCheckResult.create(
				release,
				CheckType.TEST,
				CheckStatus.PASS,
				Severity.INFO,
				"Tests passed",
				null
		);
	}
}
