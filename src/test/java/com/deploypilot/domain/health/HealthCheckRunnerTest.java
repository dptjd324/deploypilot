package com.deploypilot.domain.health;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.deploypilot.domain.health.dto.HealthCheckResultResponse;
import com.deploypilot.domain.release.Release;
import com.deploypilot.domain.release.ReleaseRepository;
import com.deploypilot.domain.serviceapp.ServiceApp;
import com.deploypilot.domain.serviceapp.ServiceAppEnvironment;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HealthCheckRunnerTest {

	@Mock
	private ReleaseRepository releaseRepository;

	@Mock
	private HealthCheckResultRepository healthCheckResultRepository;

	@Mock
	private HealthCheckClient healthCheckClient;

	@Test
	void runStoresPassResultForTwoHundredResponse() {
		HealthCheckRunner runner = new HealthCheckRunner(releaseRepository, healthCheckResultRepository, healthCheckClient);
		Release release = release("https://order.example.com/health");

		when(releaseRepository.findById(1L)).thenReturn(Optional.of(release));
		when(healthCheckClient.get("https://order.example.com/health"))
				.thenReturn(new HealthCheckResponse(200, 120, "ok"));
		when(healthCheckResultRepository.save(any(HealthCheckResult.class)))
				.thenAnswer(invocation -> invocation.getArgument(0));

		HealthCheckResultResponse response = runner.run(1L);

		assertThat(response.status()).isEqualTo(HealthCheckStatus.PASS);
		assertThat(response.httpStatus()).isEqualTo(200);
		assertThat(response.responseTimeMs()).isEqualTo(120);
	}

	@Test
	void runStoresFailResultWhenUrlIsMissing() {
		HealthCheckRunner runner = new HealthCheckRunner(releaseRepository, healthCheckResultRepository, healthCheckClient);
		Release release = release(null);

		when(releaseRepository.findById(1L)).thenReturn(Optional.of(release));
		when(healthCheckResultRepository.save(any(HealthCheckResult.class)))
				.thenAnswer(invocation -> invocation.getArgument(0));

		HealthCheckResultResponse response = runner.run(1L);

		assertThat(response.status()).isEqualTo(HealthCheckStatus.FAIL);
		assertThat(response.errorMessage()).isEqualTo("Health check URL is not configured");
	}

	private Release release(String healthCheckUrl) {
		return Release.create(
				serviceApp(healthCheckUrl),
				"v1.0.0",
				ServiceAppEnvironment.PRODUCTION,
				"main",
				"abc123",
				LocalDateTime.of(2026, 6, 20, 10, 0)
		);
	}

	private ServiceApp serviceApp(String healthCheckUrl) {
		return ServiceApp.create(
				"order-api",
				"Order service",
				"https://github.com/example/order-api",
				"main",
				"platform",
				ServiceAppEnvironment.PRODUCTION,
				healthCheckUrl
		);
	}
}
