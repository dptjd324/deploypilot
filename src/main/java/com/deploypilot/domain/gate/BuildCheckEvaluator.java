package com.deploypilot.domain.gate;

import com.deploypilot.domain.cicd.CiRun;
import com.deploypilot.domain.cicd.CiRunRepository;
import com.deploypilot.domain.cicd.CiRunStatus;
import com.deploypilot.domain.release.Release;
import org.springframework.stereotype.Component;

@Component
public class BuildCheckEvaluator implements QualityCheckEvaluator {

	private final CiRunRepository ciRunRepository;

	public BuildCheckEvaluator(CiRunRepository ciRunRepository) {
		this.ciRunRepository = ciRunRepository;
	}

	@Override
	public QualityCheckResult evaluate(Release release) {
		return ciRunRepository.findTopByReleaseIdOrderByCreatedAtDesc(release.getId())
				.map(ciRun -> evaluateCiRun(release, ciRun))
				.orElseGet(() -> QualityCheckResult.create(
						release,
						CheckType.BUILD,
						CheckStatus.WARN,
						Severity.WARNING,
						"No CI run found",
						null
				));
	}

	private QualityCheckResult evaluateCiRun(Release release, CiRun ciRun) {
		if (ciRun.getStatus() == CiRunStatus.SUCCESS) {
			return QualityCheckResult.create(
					release,
					CheckType.BUILD,
					CheckStatus.PASS,
					Severity.INFO,
					"Build succeeded",
					null
			);
		}

		if (ciRun.getStatus() == CiRunStatus.IN_PROGRESS) {
			return QualityCheckResult.create(
					release,
					CheckType.BUILD,
					CheckStatus.WARN,
					Severity.WARNING,
					"Build is still in progress",
					null
			);
		}

		return QualityCheckResult.create(
				release,
				CheckType.BUILD,
				CheckStatus.FAIL,
				Severity.BLOCKER,
				"Build failed",
				null
		);
	}
}
