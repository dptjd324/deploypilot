package com.deploypilot.domain.gate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.deploypilot.domain.metric.ReleaseMetric;
import com.deploypilot.domain.metric.ReleaseMetricRepository;
import com.deploypilot.domain.release.Release;
import com.deploypilot.domain.serviceapp.ServiceApp;
import com.deploypilot.domain.serviceapp.ServiceAppEnvironment;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MetricEvaluatorTest {

	@Mock
	private ReleaseMetricRepository releaseMetricRepository;

	@Test
	void errorRateEvaluatorReturnsBlockerWhenErrorRateIsTooHigh() {
		Release release = release();
		MetricErrorRateEvaluator evaluator = new MetricErrorRateEvaluator(releaseMetricRepository);

		when(releaseMetricRepository.findTopByReleaseIdOrderByCollectedAtDesc(release.getId()))
				.thenReturn(Optional.of(ReleaseMetric.create(release, 100, 5, 100, 200)));

		QualityCheckResult result = evaluator.evaluate(release);

		assertThat(result.getCheckType()).isEqualTo(CheckType.METRIC_ERROR_RATE);
		assertThat(result.getStatus()).isEqualTo(CheckStatus.FAIL);
		assertThat(result.getSeverity()).isEqualTo(Severity.BLOCKER);
	}

	@Test
	void responseTimeEvaluatorReturnsWarnWhenP95ResponseTimeIsSlow() {
		Release release = release();
		MetricResponseTimeEvaluator evaluator = new MetricResponseTimeEvaluator(releaseMetricRepository);

		when(releaseMetricRepository.findTopByReleaseIdOrderByCollectedAtDesc(release.getId()))
				.thenReturn(Optional.of(ReleaseMetric.create(release, 100, 0, 300, 1500)));

		QualityCheckResult result = evaluator.evaluate(release);

		assertThat(result.getCheckType()).isEqualTo(CheckType.METRIC_RESPONSE_TIME);
		assertThat(result.getStatus()).isEqualTo(CheckStatus.WARN);
		assertThat(result.getSeverity()).isEqualTo(Severity.WARNING);
	}

	private Release release() {
		return Release.create(
				serviceApp(),
				"v1.0.0",
				ServiceAppEnvironment.PRODUCTION,
				"main",
				"abc123",
				LocalDateTime.of(2026, 6, 23, 10, 0)
		);
	}

	private ServiceApp serviceApp() {
		return ServiceApp.create(
				"order-api",
				"Order service",
				"https://github.com/example/order-api",
				"main",
				"platform",
				ServiceAppEnvironment.PRODUCTION,
				"https://order.example.com/health"
		);
	}
}
