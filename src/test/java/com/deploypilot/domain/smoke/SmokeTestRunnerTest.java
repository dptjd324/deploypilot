package com.deploypilot.domain.smoke;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import com.deploypilot.domain.release.Release;
import com.deploypilot.domain.release.ReleaseRepository;
import com.deploypilot.domain.serviceapp.ServiceApp;
import com.deploypilot.domain.serviceapp.ServiceAppEnvironment;
import com.deploypilot.domain.smoke.dto.SmokeTestResultResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SmokeTestRunnerTest {

	@Mock
	private ReleaseRepository releaseRepository;

	@Mock
	private SmokeTestRepository smokeTestRepository;

	@Mock
	private SmokeTestResultRepository smokeTestResultRepository;

	@Mock
	private SmokeTestClient smokeTestClient;

	@Test
	void runStoresPassResultWhenExpectedStatusAndResponseTimeMatch() {
		SmokeTestRunner runner = new SmokeTestRunner(
				releaseRepository,
				smokeTestRepository,
				smokeTestResultRepository,
				smokeTestClient
		);
		Release release = release();
		SmokeTest smokeTest = SmokeTest.create(
				release.getServiceApp(),
				"Root endpoint",
				SmokeTestMethod.GET,
				"/",
				200,
				500,
				true,
				true
		);

		when(releaseRepository.findById(1L)).thenReturn(Optional.of(release));
		when(smokeTestRepository.findByServiceAppIdAndEnabledTrueOrderByIdAsc(release.getServiceApp().getId()))
				.thenReturn(List.of(smokeTest));
		when(smokeTestClient.execute(SmokeTestMethod.GET, "https://order.example.com/"))
				.thenReturn(new SmokeTestHttpResponse(200, 100));
		when(smokeTestResultRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

		List<SmokeTestResultResponse> responses = runner.run(1L);

		assertThat(responses).hasSize(1);
		assertThat(responses.get(0).status()).isEqualTo(SmokeTestResultStatus.PASS);
		assertThat(responses.get(0).actualStatus()).isEqualTo(200);
	}

	private Release release() {
		return Release.create(
				serviceApp(),
				"v1.0.0",
				ServiceAppEnvironment.PRODUCTION,
				"main",
				"abc123",
				LocalDateTime.of(2026, 6, 20, 10, 0)
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
