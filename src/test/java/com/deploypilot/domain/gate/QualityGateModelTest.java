package com.deploypilot.domain.gate;

import static org.assertj.core.api.Assertions.assertThat;

import com.deploypilot.domain.release.Release;
import com.deploypilot.domain.serviceapp.ServiceApp;
import com.deploypilot.domain.serviceapp.ServiceAppEnvironment;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class QualityGateModelTest {

	@Test
	void createQualityCheckResultStoresTraceableCheckData() {
		Release release = release();

		QualityCheckResult result = QualityCheckResult.create(
				release,
				CheckType.TEST,
				CheckStatus.FAIL,
				Severity.BLOCKER,
				"Tests failed",
				"{\"failedTests\":2}"
		);

		assertThat(result.getRelease()).isEqualTo(release);
		assertThat(result.getCheckType()).isEqualTo(CheckType.TEST);
		assertThat(result.getStatus()).isEqualTo(CheckStatus.FAIL);
		assertThat(result.getSeverity()).isEqualTo(Severity.BLOCKER);
		assertThat(result.getMessage()).isEqualTo("Tests failed");
		assertThat(result.getDetailJson()).isEqualTo("{\"failedTests\":2}");
	}

	@Test
	void createQualityGateResultStoresReleaseDecision() {
		Release release = release();

		QualityGateResult result = QualityGateResult.create(release, GateResult.WARN, "Warnings exist");

		assertThat(result.getRelease()).isEqualTo(release);
		assertThat(result.getResult()).isEqualTo(GateResult.WARN);
		assertThat(result.getReason()).isEqualTo("Warnings exist");
	}

	private Release release() {
		return Release.create(
				serviceApp(),
				"v1.0.0",
				ServiceAppEnvironment.PRODUCTION,
				"main",
				"abc123",
				LocalDateTime.of(2026, 6, 19, 10, 0)
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
