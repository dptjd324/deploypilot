package com.deploypilot.domain.gate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.deploypilot.domain.release.Release;
import com.deploypilot.domain.serviceapp.ServiceApp;
import com.deploypilot.domain.serviceapp.ServiceAppEnvironment;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class QualityGateServiceTest {

	@Mock
	private QualityGateEngine qualityGateEngine;

	@Mock
	private QualityGateResultRepository qualityGateResultRepository;

	@Mock
	private QualityCheckResultRepository qualityCheckResultRepository;

	@Test
	void runExecutesEngineAndReturnsGateResultResponse() {
		Release release = release();
		QualityGateResult gateResult = QualityGateResult.create(release, GateResult.PASS, "All quality checks passed");
		QualityGateService service = service();

		when(qualityGateEngine.run(1L)).thenReturn(gateResult);

		var response = service.run(1L);

		assertThat(response.releaseId()).isEqualTo(release.getId());
		assertThat(response.result()).isEqualTo(GateResult.PASS);
		assertThat(response.reason()).isEqualTo("All quality checks passed");
	}

	@Test
	void findLatestResultReturnsNewestGateResultResponse() {
		Release release = release();
		QualityGateResult gateResult = QualityGateResult.create(release, GateResult.WARN, "Warnings exist");
		QualityGateService service = service();

		when(qualityGateResultRepository.findTopByReleaseIdOrderByCheckedAtDesc(1L)).thenReturn(Optional.of(gateResult));

		var response = service.findLatestResult(1L);

		assertThat(response.releaseId()).isEqualTo(release.getId());
		assertThat(response.result()).isEqualTo(GateResult.WARN);
		assertThat(response.reason()).isEqualTo("Warnings exist");
	}

	@Test
	void findCheckResultsReturnsCheckResultResponses() {
		Release release = release();
		QualityCheckResult checkResult = QualityCheckResult.create(
				release,
				CheckType.BUILD,
				CheckStatus.FAIL,
				Severity.BLOCKER,
				"Build failed",
				null
		);
		QualityGateService service = service();

		when(qualityCheckResultRepository.findByReleaseIdOrderByCheckedAtDesc(1L)).thenReturn(List.of(checkResult));

		var responses = service.findCheckResults(1L);

		assertThat(responses).hasSize(1);
		assertThat(responses.get(0).releaseId()).isEqualTo(release.getId());
		assertThat(responses.get(0).checkType()).isEqualTo(CheckType.BUILD);
		assertThat(responses.get(0).status()).isEqualTo(CheckStatus.FAIL);
		assertThat(responses.get(0).severity()).isEqualTo(Severity.BLOCKER);
	}

	private QualityGateService service() {
		return new QualityGateService(qualityGateEngine, qualityGateResultRepository, qualityCheckResultRepository);
	}

	private Release release() {
		Release release = Release.create(
				serviceApp(),
				"v1.0.0",
				ServiceAppEnvironment.PRODUCTION,
				"main",
				"abc123",
				LocalDateTime.of(2026, 6, 19, 10, 0)
		);
		ReflectionTestUtils.setField(release, "id", 1L);
		return release;
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
