package com.deploypilot.domain.gate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.deploypilot.domain.release.Release;
import com.deploypilot.domain.release.ReleaseRepository;
import com.deploypilot.domain.serviceapp.ServiceApp;
import com.deploypilot.domain.serviceapp.ServiceAppEnvironment;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class QualityGateEngineTest {

	@Mock
	private ReleaseRepository releaseRepository;

	@Mock
	private QualityCheckResultRepository qualityCheckResultRepository;

	@Mock
	private QualityGateResultRepository qualityGateResultRepository;

	@Test
	void decideReturnsFailWhenBlockerCheckFails() {
		QualityGateEngine engine = engine(List.of());
		Release release = release();

		GateResult result = engine.decide(List.of(
				QualityCheckResult.create(release, CheckType.BUILD, CheckStatus.FAIL, Severity.BLOCKER, "Build failed", null),
				QualityCheckResult.create(release, CheckType.COVERAGE, CheckStatus.WARN, Severity.WARNING, "Low coverage", null)
		));

		assertThat(result).isEqualTo(GateResult.FAIL);
	}

	@Test
	void decideReturnsWarnWhenOnlyWarningsExist() {
		QualityGateEngine engine = engine(List.of());
		Release release = release();

		GateResult result = engine.decide(List.of(
				QualityCheckResult.create(release, CheckType.BUILD, CheckStatus.PASS, Severity.INFO, "Build passed", null),
				QualityCheckResult.create(release, CheckType.COVERAGE, CheckStatus.WARN, Severity.WARNING, "Low coverage", null)
		));

		assertThat(result).isEqualTo(GateResult.WARN);
	}

	@Test
	void decideReturnsPassWhenAllChecksPass() {
		QualityGateEngine engine = engine(List.of());
		Release release = release();

		GateResult result = engine.decide(List.of(
				QualityCheckResult.create(release, CheckType.BUILD, CheckStatus.PASS, Severity.INFO, "Build passed", null),
				QualityCheckResult.create(release, CheckType.TEST, CheckStatus.PASS, Severity.INFO, "Tests passed", null)
		));

		assertThat(result).isEqualTo(GateResult.PASS);
	}

	@Test
	@SuppressWarnings("unchecked")
	void runSavesCheckResultsGateResultAndUpdatesReleaseGateStatus() {
		Release release = release();
		QualityCheckEvaluator passingEvaluator = target -> QualityCheckResult.create(
				target,
				CheckType.BUILD,
				CheckStatus.PASS,
				Severity.INFO,
				"Build passed",
				null
		);
		QualityGateEngine engine = engine(List.of(passingEvaluator));

		when(releaseRepository.findById(1L)).thenReturn(Optional.of(release));
		when(qualityGateResultRepository.save(any(QualityGateResult.class))).thenAnswer(invocation -> invocation.getArgument(0));

		QualityGateResult result = engine.run(1L);

		ArgumentCaptor<List<QualityCheckResult>> captor = ArgumentCaptor.forClass(List.class);
		verify(qualityCheckResultRepository).saveAll(captor.capture());
		assertThat(captor.getValue()).hasSize(1);
		assertThat(result.getResult()).isEqualTo(GateResult.PASS);
		assertThat(release.getGateStatus().name()).isEqualTo(GateResult.PASS.name());
	}

	private QualityGateEngine engine(List<QualityCheckEvaluator> evaluators) {
		return new QualityGateEngine(
				evaluators,
				releaseRepository,
				qualityCheckResultRepository,
				qualityGateResultRepository
		);
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
