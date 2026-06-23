package com.deploypilot.domain.metric;

import static org.assertj.core.api.Assertions.assertThat;

import com.deploypilot.domain.release.Release;
import com.deploypilot.domain.serviceapp.ServiceApp;
import com.deploypilot.domain.serviceapp.ServiceAppEnvironment;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class ReleaseMetricTest {

	@Test
	void createCalculatesErrorRateFromTotalRequestsAndErrors() {
		ReleaseMetric metric = ReleaseMetric.create(release(), 1000, 25, 120, 300);

		assertThat(metric.getErrorRate()).isEqualByComparingTo(new BigDecimal("2.50"));
	}

	@Test
	void createUsesZeroErrorRateWhenTotalRequestsIsZero() {
		ReleaseMetric metric = ReleaseMetric.create(release(), 0, 0, 0, 0);

		assertThat(metric.getErrorRate()).isEqualByComparingTo(new BigDecimal("0.00"));
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
