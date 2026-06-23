package com.deploypilot.domain.gate;

import com.deploypilot.domain.metric.ReleaseMetric;
import com.deploypilot.domain.metric.ReleaseMetricRepository;
import com.deploypilot.domain.release.Release;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public class MetricErrorRateEvaluator implements QualityCheckEvaluator {

	private static final BigDecimal WARNING_ERROR_RATE = new BigDecimal("1.00");
	private static final BigDecimal BLOCKER_ERROR_RATE = new BigDecimal("5.00");

	private final ReleaseMetricRepository releaseMetricRepository;

	public MetricErrorRateEvaluator(ReleaseMetricRepository releaseMetricRepository) {
		this.releaseMetricRepository = releaseMetricRepository;
	}

	@Override
	public QualityCheckResult evaluate(Release release) {
		return releaseMetricRepository.findTopByReleaseIdOrderByCollectedAtDesc(release.getId())
				.map(metric -> evaluateMetric(release, metric))
				.orElseGet(() -> QualityCheckResult.create(
						release,
						CheckType.METRIC_ERROR_RATE,
						CheckStatus.WARN,
						Severity.WARNING,
						"No release metric found",
						null
				));
	}

	private QualityCheckResult evaluateMetric(Release release, ReleaseMetric metric) {
		if (metric.getErrorRate().compareTo(BLOCKER_ERROR_RATE) >= 0) {
			return QualityCheckResult.create(
					release,
					CheckType.METRIC_ERROR_RATE,
					CheckStatus.FAIL,
					Severity.BLOCKER,
					"Error rate is above blocker threshold",
					"{\"errorRate\":" + metric.getErrorRate() + ",\"threshold\":5.00}"
			);
		}

		if (metric.getErrorRate().compareTo(WARNING_ERROR_RATE) >= 0) {
			return QualityCheckResult.create(
					release,
					CheckType.METRIC_ERROR_RATE,
					CheckStatus.WARN,
					Severity.WARNING,
					"Error rate is above warning threshold",
					"{\"errorRate\":" + metric.getErrorRate() + ",\"threshold\":1.00}"
			);
		}

		return QualityCheckResult.create(
				release,
				CheckType.METRIC_ERROR_RATE,
				CheckStatus.PASS,
				Severity.INFO,
				"Error rate is within threshold",
				"{\"errorRate\":" + metric.getErrorRate() + "}"
		);
	}
}
