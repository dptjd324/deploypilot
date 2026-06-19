package com.deploypilot.domain.gate;

import com.deploypilot.domain.cicd.CiRun;
import com.deploypilot.domain.cicd.CiRunRepository;
import com.deploypilot.domain.release.Release;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public class CoverageCheckEvaluator implements QualityCheckEvaluator {

	private static final BigDecimal MINIMUM_COVERAGE = new BigDecimal("80.00");

	private final CiRunRepository ciRunRepository;

	public CoverageCheckEvaluator(CiRunRepository ciRunRepository) {
		this.ciRunRepository = ciRunRepository;
	}

	@Override
	public QualityCheckResult evaluate(Release release) {
		return ciRunRepository.findTopByReleaseIdOrderByCreatedAtDesc(release.getId())
				.map(ciRun -> evaluateCiRun(release, ciRun))
				.orElseGet(() -> QualityCheckResult.create(
						release,
						CheckType.COVERAGE,
						CheckStatus.WARN,
						Severity.WARNING,
						"No coverage result found",
						null
				));
	}

	private QualityCheckResult evaluateCiRun(Release release, CiRun ciRun) {
		BigDecimal coverage = ciRun.getCoverage();
		if (coverage == null) {
			return QualityCheckResult.create(
					release,
					CheckType.COVERAGE,
					CheckStatus.WARN,
					Severity.WARNING,
					"Coverage was not reported",
					null
			);
		}

		if (coverage.compareTo(MINIMUM_COVERAGE) < 0) {
			return QualityCheckResult.create(
					release,
					CheckType.COVERAGE,
					CheckStatus.WARN,
					Severity.WARNING,
					"Coverage is below threshold",
					"{\"coverage\":" + coverage + ",\"minimum\":80.00}"
			);
		}

		return QualityCheckResult.create(
				release,
				CheckType.COVERAGE,
				CheckStatus.PASS,
				Severity.INFO,
				"Coverage meets threshold",
				"{\"coverage\":" + coverage + ",\"minimum\":80.00}"
		);
	}
}
