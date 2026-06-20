package com.deploypilot.domain.risk;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.deploypilot.domain.gate.CheckStatus;
import com.deploypilot.domain.gate.Severity;
import com.deploypilot.domain.release.Release;
import com.deploypilot.domain.release.ReleaseRepository;
import com.deploypilot.domain.risk.dto.RiskCheckRequest;
import com.deploypilot.domain.risk.dto.RiskCheckResultResponse;
import com.deploypilot.domain.serviceapp.ServiceApp;
import com.deploypilot.domain.serviceapp.ServiceAppEnvironment;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RiskCheckServiceTest {

	@Mock
	private ReleaseRepository releaseRepository;

	@Mock
	private RiskCheckResultRepository riskCheckResultRepository;

	@Test
	void checkDbMigrationStoresRiskResult() {
		RiskCheckService service = service();

		when(releaseRepository.findById(1L)).thenReturn(Optional.of(release()));
		when(riskCheckResultRepository.save(any(RiskCheckResult.class)))
				.thenAnswer(invocation -> invocation.getArgument(0));

		RiskCheckResultResponse response = service.checkDbMigration(
				1L,
				new RiskCheckRequest("DROP TABLE orders;")
		);

		assertThat(response.riskType()).isEqualTo(RiskType.DB_MIGRATION);
		assertThat(response.status()).isEqualTo(CheckStatus.FAIL);
		assertThat(response.severity()).isEqualTo(Severity.BLOCKER);
		assertThat(response.detectedPattern()).isEqualTo("DROP TABLE");
	}

	@Test
	void checkSecurityConfigStoresRiskResult() {
		RiskCheckService service = service();

		when(releaseRepository.findById(1L)).thenReturn(Optional.of(release()));
		when(riskCheckResultRepository.save(any(RiskCheckResult.class)))
				.thenAnswer(invocation -> invocation.getArgument(0));

		RiskCheckResultResponse response = service.checkSecurityConfig(
				1L,
				new RiskCheckRequest("requestMatchers(\"/**\").permitAll()")
		);

		assertThat(response.riskType()).isEqualTo(RiskType.SECURITY_CONFIG);
		assertThat(response.status()).isEqualTo(CheckStatus.FAIL);
		assertThat(response.severity()).isEqualTo(Severity.BLOCKER);
		assertThat(response.detectedPattern()).isEqualTo("PERMITALL");
	}

	private RiskCheckService service() {
		return new RiskCheckService(
				releaseRepository,
				riskCheckResultRepository,
				new DbMigrationRiskChecker(),
				new SecurityConfigRiskChecker()
		);
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
