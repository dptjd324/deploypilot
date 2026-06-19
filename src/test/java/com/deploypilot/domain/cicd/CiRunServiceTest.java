package com.deploypilot.domain.cicd;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.deploypilot.domain.cicd.dto.CiRunResponse;
import com.deploypilot.domain.cicd.dto.GitHubActionsRunRequest;
import com.deploypilot.domain.release.GateStatus;
import com.deploypilot.domain.release.Release;
import com.deploypilot.domain.release.ReleaseRepository;
import com.deploypilot.domain.release.ReleaseStatus;
import com.deploypilot.domain.serviceapp.ServiceApp;
import com.deploypilot.domain.serviceapp.ServiceAppEnvironment;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class CiRunServiceTest {

	@Mock
	private CiRunRepository ciRunRepository;

	@Mock
	private ReleaseRepository releaseRepository;

	private final WebhookSecretProperties webhookSecretProperties = new WebhookSecretProperties("secret");

	@Test
	void saveGitHubActionsRunSavesCiRunAndMarksReleaseCiSuccess() {
		CiRunService service = new CiRunService(ciRunRepository, releaseRepository, webhookSecretProperties);
		Release release = release();
		GitHubActionsRunRequest request = request(CiRunStatus.SUCCESS);

		when(releaseRepository.findByServiceApp_NameAndVersionAndBranchAndCommitHash(
				"order-api",
				"v1.0.0",
				"main",
				"abc123"
		)).thenReturn(Optional.of(release));
		when(ciRunRepository.save(any(CiRun.class))).thenAnswer(invocation -> invocation.getArgument(0));

		CiRunResponse response = service.saveGitHubActionsRun("secret", request);

		assertThat(response.provider()).isEqualTo(CiProvider.GITHUB_ACTIONS);
		assertThat(response.status()).isEqualTo(CiRunStatus.SUCCESS);
		assertThat(response.durationSeconds()).isEqualTo(210);
		assertThat(release.getStatus()).isEqualTo(ReleaseStatus.CI_SUCCESS);
		assertThat(release.getGateStatus()).isEqualTo(GateStatus.PENDING);
	}

	@Test
	void saveGitHubActionsRunMarksReleaseCiFailedForFailure() {
		CiRunService service = new CiRunService(ciRunRepository, releaseRepository, webhookSecretProperties);
		Release release = release();

		when(releaseRepository.findByServiceApp_NameAndVersionAndBranchAndCommitHash(
				"order-api",
				"v1.0.0",
				"main",
				"abc123"
		)).thenReturn(Optional.of(release));
		when(ciRunRepository.save(any(CiRun.class))).thenAnswer(invocation -> invocation.getArgument(0));

		service.saveGitHubActionsRun("secret", request(CiRunStatus.FAILURE));

		assertThat(release.getStatus()).isEqualTo(ReleaseStatus.CI_FAILED);
		assertThat(release.getFailedAt()).isNotNull();
	}

	@Test
	void saveGitHubActionsRunRejectsInvalidToken() {
		CiRunService service = new CiRunService(ciRunRepository, releaseRepository, webhookSecretProperties);

		assertThatThrownBy(() -> service.saveGitHubActionsRun("wrong", request(CiRunStatus.SUCCESS)))
				.isInstanceOf(ResponseStatusException.class)
				.extracting("statusCode")
				.isEqualTo(HttpStatus.UNAUTHORIZED);
		verify(ciRunRepository, never()).save(any());
	}

	@Test
	void saveGitHubActionsRunRejectsMissingRelease() {
		CiRunService service = new CiRunService(ciRunRepository, releaseRepository, webhookSecretProperties);

		when(releaseRepository.findByServiceApp_NameAndVersionAndBranchAndCommitHash(
				"order-api",
				"v1.0.0",
				"main",
				"abc123"
		)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.saveGitHubActionsRun("secret", request(CiRunStatus.SUCCESS)))
				.isInstanceOf(ResponseStatusException.class)
				.extracting("statusCode")
				.isEqualTo(HttpStatus.NOT_FOUND);
	}

	private GitHubActionsRunRequest request(CiRunStatus status) {
		return new GitHubActionsRunRequest(
				"order-api",
				"v1.0.0",
				"main",
				"abc123",
				"CI",
				"123",
				status,
				120,
				status == CiRunStatus.SUCCESS ? 120 : 110,
				status == CiRunStatus.SUCCESS ? 0 : 10,
				new BigDecimal("78.40"),
				"https://github.com/example/order-api/actions/runs/123",
				LocalDateTime.of(2026, 6, 19, 10, 0),
				LocalDateTime.of(2026, 6, 19, 10, 3, 30)
		);
	}

	private Release release() {
		return Release.create(
				serviceApp(),
				"v1.0.0",
				ServiceAppEnvironment.PRODUCTION,
				"main",
				"abc123",
				LocalDateTime.of(2026, 6, 19, 9, 50)
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
