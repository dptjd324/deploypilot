package com.deploypilot.domain.cicd;

import com.deploypilot.domain.cicd.dto.CiRunResponse;
import com.deploypilot.domain.cicd.dto.GitHubActionsRunRequest;
import com.deploypilot.domain.release.Release;
import com.deploypilot.domain.release.ReleaseRepository;
import java.util.List;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@EnableConfigurationProperties(WebhookSecretProperties.class)
public class CiRunService {

	private final CiRunRepository ciRunRepository;
	private final ReleaseRepository releaseRepository;
	private final WebhookSecretProperties webhookSecretProperties;

	public CiRunService(
			CiRunRepository ciRunRepository,
			ReleaseRepository releaseRepository,
			WebhookSecretProperties webhookSecretProperties
	) {
		this.ciRunRepository = ciRunRepository;
		this.releaseRepository = releaseRepository;
		this.webhookSecretProperties = webhookSecretProperties;
	}

	@Transactional
	public CiRunResponse saveGitHubActionsRun(String token, GitHubActionsRunRequest request) {
		validateSecret(token);

		Release release = releaseRepository.findByServiceApp_NameAndVersionAndBranchAndCommitHash(
						request.serviceName(),
						request.version(),
						request.branch(),
						request.commitHash()
				)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Release not found"));

		CiRun ciRun = CiRun.create(
				release,
				CiProvider.GITHUB_ACTIONS,
				request.workflowName(),
				request.runId(),
				request.status(),
				request.totalTests(),
				request.passedTests(),
				request.failedTests(),
				request.coverage(),
				request.startedAt(),
				request.finishedAt(),
				request.logUrl()
		);

		CiRun savedCiRun = ciRunRepository.save(ciRun);
		release.applyCiResult(
				savedCiRun.getStartedAt(),
				savedCiRun.getFinishedAt(),
				savedCiRun.isCompleted(),
				savedCiRun.isSuccessful()
		);

		return CiRunResponse.from(savedCiRun);
	}

	@Transactional(readOnly = true)
	public List<CiRunResponse> findRecentRuns() {
		return ciRunRepository.findTop50ByOrderByCreatedAtDesc()
				.stream()
				.map(CiRunResponse::from)
				.toList();
	}

	@Transactional(readOnly = true)
	public List<CiRunResponse> findByReleaseId(Long releaseId) {
		return ciRunRepository.findByReleaseIdOrderByCreatedAtDesc(releaseId)
				.stream()
				.map(CiRunResponse::from)
				.toList();
	}

	private void validateSecret(String token) {
		if (!webhookSecretProperties.matches(token)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid webhook token");
		}
	}
}
