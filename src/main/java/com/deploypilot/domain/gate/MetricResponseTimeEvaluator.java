package com.deploypilot.domain.gate;

import com.deploypilot.domain.metric.ReleaseMetric;
import com.deploypilot.domain.metric.ReleaseMetricRepository;
import com.deploypilot.domain.release.Release;
import org.springframework.stereotype.Component;

@Component
public class MetricResponseTimeEvaluator implements QualityCheckEvaluator {

	private static final long WARNING_P95_RESPONSE_TIME_MS = 1000;
	private static final long BLOCKER_P95_RESPONSE_TIME_MS = 3000;

	private final ReleaseMetricRepository releaseMetricRepository;

	public MetricResponseTimeEvaluator(ReleaseMetricRepository releaseMetricRepository) {
		this.releaseMetricRepository = releaseMetricRepository;
	}

	@Override
	public QualityCheckResult evaluate(Release release) {
		return releaseMetricRepository.findTopByReleaseIdOrderByCollectedAtDesc(release.getId())
				.map(metric -> evaluateMetric(release, metric))
				.orElseGet(() -> QualityCheckResult.create(
						release,
						CheckType.METRIC_RESPONSE_TIME,
						CheckStatus.WARN,
						Severity.WARNING,
						"No release metric found",
						null
				));
	}

	private QualityCheckResult evaluateMetric(Release release, ReleaseMetric metric) {
		if (metric.getP95ResponseTimeMs() >= BLOCKER_P95_RESPONSE_TIME_MS) {
			return QualityCheckResult.create(
					release,
					CheckType.METRIC_RESPONSE_TIME,
					CheckStatus.FAIL,
					Severity.BLOCKER,
					"P95 response time is above blocker threshold",
					"{\"p95ResponseTimeMs\":" + metric.getP95ResponseTimeMs() + ",\"threshold\":3000}"
			);
		}

		if (metric.getP95ResponseTimeMs() >= WARNING_P95_RESPONSE_TIME_MS) {
			return QualityCheckResult.create(
					release,
					CheckType.METRIC_RESPONSE_TIME,
					CheckStatus.WARN,
					Severity.WARNING,
					"P95 response time is above warning threshold",
					"{\"p95ResponseTimeMs\":" + metric.getP95ResponseTimeMs() + ",\"threshold\":1000}"
			);
		}

		return QualityCheckResult.create(
				release,
				CheckType.METRIC_RESPONSE_TIME,
				CheckStatus.PASS,
				Severity.INFO,
				"Response time is within threshold",
				"{\"p95ResponseTimeMs\":" + metric.getP95ResponseTimeMs() + "}"
		);
	}
}
