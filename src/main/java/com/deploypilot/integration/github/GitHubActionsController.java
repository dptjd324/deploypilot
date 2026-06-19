package com.deploypilot.integration.github;

import com.deploypilot.domain.cicd.CiRunService;
import com.deploypilot.domain.cicd.dto.CiRunResponse;
import com.deploypilot.domain.cicd.dto.GitHubActionsRunRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/integrations/github-actions")
public class GitHubActionsController {

	private static final String WEBHOOK_TOKEN_HEADER = "X-DeployPilot-Token";

	private final CiRunService ciRunService;

	public GitHubActionsController(CiRunService ciRunService) {
		this.ciRunService = ciRunService;
	}

	@PostMapping("/runs")
	@ResponseStatus(HttpStatus.CREATED)
	public CiRunResponse saveRun(
			@RequestHeader(value = WEBHOOK_TOKEN_HEADER, required = false) String token,
			@Valid @RequestBody GitHubActionsRunRequest request
	) {
		return ciRunService.saveGitHubActionsRun(token, request);
	}
}
